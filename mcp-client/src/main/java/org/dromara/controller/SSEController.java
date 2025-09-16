package org.dromara.controller;

import org.dromara.enums.SSEMsgType;
import org.dromara.utils.SSEServer;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * @author lee
 * @description
 */
@RestController
@RequestMapping("/sse")
public class SSEController {

    /**
     * 建立SSE连接
     *
     * @param userId 用户唯一标识符，用于建立特定用户的SSE连接
     * @return SseEmitter SSE连接发射器，用于向客户端推送事件流数据
     */
    @GetMapping(path = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter connect(@RequestParam String userId) {
        return SSEServer.connect(userId);
    }

    /**
     * SSE发送单个消息
     *
     * @param userId
     * @param message
     * @return
     */
    @GetMapping(path = "/sendMessage")
    public Object sendMessage(@RequestParam String userId, @RequestParam String message) {
        SSEServer.sendMsg(userId, message, SSEMsgType.MESSAGE);
        return "OK";
    }

}
