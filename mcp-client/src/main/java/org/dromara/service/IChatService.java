package org.dromara.service;

import org.dromara.bean.ChatEntity;
import org.springframework.ai.chat.model.ChatResponse;
import reactor.core.publisher.Flux;

/**
 * @author lee
 * @description
 */
public interface IChatService {

    /**
     * 测试大模型交互聊天
     *
     * @param prompt
     * @return
     */
    String chatTest(String prompt);

    /**
     * 测试大模型交互聊天 （Flux流式输出 - JSON）
     *
     * @param prompt
     * @return
     */
    Flux<ChatResponse> streamResponse(String prompt);

    /**
     * 测试大模型交互聊天 （Flux流式输出 - String）
     *
     * @param prompt
     * @return
     */
    Flux<String> streamStr(String prompt);

    /**
     * 和大模型交互
     * @param chatEntity
     * @return
     */
    void deepSeekChat(ChatEntity chatEntity);

}
