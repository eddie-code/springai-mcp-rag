package org.dromara.service.impl;

import org.dromara.service.IChatService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

/**
 * @author lee
 * @description
 */
@Service
public class ChatServiceImpl implements IChatService {

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
        return chatClient.prompt(prompt).call().content();
    }

    @Override
    public Flux<ChatResponse> streamResponse(String prompt) {
        return chatClient.prompt(prompt).stream().chatResponse();
    }

    @Override
    public Flux<String> streamStr(String prompt) {
        return chatClient.prompt(prompt).stream().content();
    }
}
