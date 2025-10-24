package org.dromara.service.impl;

import cn.hutool.json.JSONUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.dromara.bean.ChatEntity;
import org.dromara.bean.ChatResponseEntity;
import org.dromara.bean.SearchResult;
import org.dromara.enums.SSEMsgType;
import org.dromara.service.IChatService;
import org.dromara.service.ISearXngService;
import org.dromara.utils.SSEServer;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lee
 * @description
 */
@Slf4j
@Service
public class ChatServiceImpl implements IChatService {

    @Resource
    private ISearXngService searXngService;

    private ChatClient chatClient;

    private String systemPrompt = "你是一个非常聪明的人工智能助手，可以帮我解决很多问题，我为你取一个名字，你的名字叫‘小爱同学’";

    // 构造器注入，自动配置方式（推荐）
    public ChatServiceImpl(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder
                .defaultSystem(systemPrompt) // 可选项： 系统提示词而已， 不加就会返回 DeepSeek 的默认回复
                .build();
    }

    /**
     * 提示词的三大类型
     * 1、system
     * 2. user
     * 3. assistant
     */

    @Override
    public String chatTest(String prompt) {
//        try {
//            Thread.sleep(6000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
        return chatClient.prompt(prompt).call().content();
    }

    @Override
    public Flux<ChatResponse> streamResponse(String prompt) {
//        try {
//            Thread.sleep(6000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
        return chatClient.prompt(prompt).stream().chatResponse();
    }

    @Override
    public Flux<String> streamStr(String prompt) {
//        try {
//            Thread.sleep(6000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
        return chatClient.prompt(prompt).stream().content();
    }

    /**
     * 执行DeepSeek聊天对话处理
     *
     * @param chatEntity 聊天实体对象，包含用户信息、消息内容和机器人消息ID等信息
     */
    @Override
    public void deepSeekChat(ChatEntity chatEntity) {

        String userId = chatEntity.getCurrentUserName();
        String prompt = chatEntity.getMessage();
        String botMsgId = chatEntity.getBotMsgId();

        // 调用chatClient获取DeepSeek的流式响应
        Flux<String> stringFlux = chatClient.prompt(prompt).stream().content();

        // 处理流式响应数据，逐个发送给客户端并收集完整内容
        List<String> list = stringFlux.toStream().map(chatResponse -> {
            String content = chatResponse.toString();
            SSEServer.sendMsg(userId, content, SSEMsgType.ADD);
            log.info("content:{}", content);
            return content;
        }).collect(Collectors.toList());

        // 将所有响应内容拼接成完整字符串
        String fullContent = list.stream().collect(Collectors.joining());
        // fullContent 可以保存到数据库, 看业务需求

        // 构造最终响应实体并发送完成消息给客户端
        ChatResponseEntity chatResponseEntity = new ChatResponseEntity(fullContent, botMsgId);

        SSEServer.sendMsg(userId, JSONUtil.toJsonStr(chatResponseEntity), SSEMsgType.FINISH);
    }


    // Dify 智能体引擎构建平台
    private static final String RAG_PROMPT = """
            基于上下文的知识库内容回答问题：
            【上下文】
            {context}
            
            【问题】
            {question}
            
            【输出】
            如果没有查到，请回复：不知道
            如果查到，请回复具体的内容。不相关的近似内容不必提到。
            """;

    @Override
    public void doChatRagSearch(ChatEntity chatEntity, List<Document> ragContext) {
        // 获取当前用户标识、问题内容和机器人消息ID
        String userId = chatEntity.getCurrentUserName();
        String question = chatEntity.getMessage();
        String botMsgId = chatEntity.getBotMsgId();

        // 构建提示词上下文
        String context = null;
        if (ragContext != null && !ragContext.isEmpty()) {
            context = ragContext.stream()
                    .map(Document::getText)
                    .collect(Collectors.joining("\n"));
        }
        // 组装完整的提示词
        assert context != null;
        Prompt prompt = new Prompt(RAG_PROMPT
                .replace("{context}", context)
                .replace("{question}", question)
        );

        System.out.println(prompt.toString());

        // 发送提示词到AI模型并获取流式响应
        Flux<String> stringFlux = chatClient.prompt(prompt).stream().content();

        // 处理流式响应并将内容逐段发送给客户端
        List<String> list = stringFlux.toStream().map(chatResponse -> {
            String content = chatResponse.toString();
            SSEServer.sendMsg(userId, content, SSEMsgType.ADD);
            log.info("content:{}", content);
            return content;
        }).collect(Collectors.toList());

        // 拼接完整响应内容
        String fullContent = String.join("", list);
        // 可以保存到数据库

        // 构造最终响应实体并发送完成消息给客户端
        ChatResponseEntity chatResponseEntity = new ChatResponseEntity(fullContent, botMsgId);

        SSEServer.sendMsg(userId, JSONUtil.toJsonStr(chatResponseEntity), SSEMsgType.FINISH);
    }

    // 定义SearXNG搜索的提示词模板，用于构建基于互联网搜索结果的回答
    private static final String SEARXNG_PROMPT = """
            你是一个互联网搜索大师，请基于以下互联网返回的结果作为上下文，根据你的理解结合用户的提问综合后，生成并且输出专业的回答：
            【上下文】
            {context}
            
            【问题】
            {question}
            
            【输出】
            如果没有查到，请回复：不知道
            如果查到，请回复具体的内容。
            """;

    /**
     * 执行基于互联网搜索的问答处理
     *
     * @param chatEntity 聊天实体对象，包含用户信息、消息内容和机器人消息ID等信息
     */
    @Override
    public void doInternetSearch(ChatEntity chatEntity) {

        // 获取当前用户标识
        String userId = chatEntity.getCurrentUserName();
        // 获取用户提出的问题
        String question = chatEntity.getMessage();
        // 获取机器人消息ID，用于标识本次对话
        String botMsgId = chatEntity.getBotMsgId();

        // 调用SearXNG服务执行互联网搜索，获取搜索结果列表
        List<SearchResult> searchResults = searXngService.search(question);

        // 构建完整的提示词，包含搜索结果作为上下文和用户问题
        String finalPrompt = buildSearXNGPrompt(question, searchResults);

        // 使用构建好的提示词创建Prompt对象
        Prompt prompt = new Prompt(finalPrompt);

        // 打印提示词内容到控制台，便于调试
        log.info("打印提示词内容到控制台，便于调试, prompt: {}", prompt.toString());

        // 发送提示词到AI模型并获取流式响应
        Flux<String> stringFlux = chatClient.prompt(prompt).stream().content();

        // 处理流式响应数据，逐个发送给客户端并收集完整内容
        List<String> list = stringFlux.toStream().map(chatResponse -> {
            // 将响应转换为字符串格式
            String content = chatResponse.toString();
            // 通过SSE将内容实时发送给指定用户
            SSEServer.sendMsg(userId, content, SSEMsgType.ADD);
            // 记录响应内容到日志
            log.info("content:{}", content);
            // 返回内容用于后续处理
            return content;
        }).toList();

        // 将所有响应内容片段拼接成完整字符串
        String fullContent = list.stream().collect(Collectors.joining());
        // fullContent 可以保存到数据库

        // 构造最终响应实体对象，包含完整内容和机器人消息ID
        ChatResponseEntity chatResponseEntity = new ChatResponseEntity(fullContent, botMsgId);

        // 通过SSE向客户端发送完成消息，表示响应已完成
        SSEServer.sendMsg(userId, JSONUtil.toJsonStr(chatResponseEntity), SSEMsgType.FINISH);

    }

    /**
     * 构建SearXNG搜索的提示词
     *
     * @param question       用户提出的问题
     * @param searchResults  SearXNG搜索结果列表
     * @return 格式化后的完整提示词字符串
     */
    private static String buildSearXNGPrompt(String question, List<SearchResult> searchResults) {

        // 创建StringBuilder用于构建上下文内容
        StringBuilder context = new StringBuilder();

        // 遍历搜索结果，将每个结果格式化后添加到上下文中
        searchResults.forEach(searchResult -> {
            // 按照指定格式追加每个搜索结果，包括来源URL和内容摘要
            context.append(String.format("<context>\n[来源] %s \n [摘要] %s \n </context>\n",
                    searchResult.getUrl(),
                    searchResult.getContent()));
        });

        log.info("将每个结果格式化后添加到上下文中, format: {}", context);

        // 将上下文和问题替换到提示词模板中，生成最终提示词
        String replace = SEARXNG_PROMPT
                .replace("{context}", context.toString())
                .replace("{question}", question);

        log.info("将上下文和问题替换到提示词模板中，生成最终提示词, replace: {}", replace);

        return replace;
    }


}
