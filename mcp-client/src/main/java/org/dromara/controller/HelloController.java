package org.dromara.controller;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import org.dromara.service.IChatService;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

/**
 * @author lee
 * @description
 */
@RestController
@RequestMapping("/hello")
public class HelloController {

    @Autowired
    private IChatService chatService;

    private final EmbeddingModel embeddingModel;

    @Autowired
    public HelloController(EmbeddingModel embeddingModel) {
        this.embeddingModel = embeddingModel;
    }

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

    @GetMapping("/ai/embedding")
    public Map embed(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        System.out.println("Embedding model class: " + embeddingModel.getClass().getName());
        System.out.println("Embedding model dimensions: " + embeddingModel.dimensions());
        EmbeddingResponse embeddingResponse = this.embeddingModel.embedForResponse(List.of(message));
        return Map.of("embedding", embeddingResponse);
    }

}
