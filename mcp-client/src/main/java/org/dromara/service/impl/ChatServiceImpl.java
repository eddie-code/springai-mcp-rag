package org.dromara.service.impl;

import org.dromara.service.IChatService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

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

    @Override
    public String chatTest(String prompt) {
        return chatClient.prompt(prompt).call().content();
    }
}
