package org.dromara.controller;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import org.dromara.service.IChatService;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * @author lee
 * @description
 */
@RestController
@RequestMapping("/hello")
public class HelloController {

    @Autowired
    private IChatService chatService;

    @RequestMapping("/world")
    public String hello() {
        return "hello Spring Ai";
    }

    @RequestMapping("/chat")
    public String chat(String msg) {
        return chatService.chatTest(msg);
    }

    @RequestMapping("/chat/response")
    public Flux<ChatResponse> chatResponse(String msg, HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        return chatService.streamResponse(msg);
    }

    @RequestMapping("/chat/stream/str")
    public Flux<String> chatStreamStr(String msg, HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        return chatService.streamStr(msg);
    }
}
