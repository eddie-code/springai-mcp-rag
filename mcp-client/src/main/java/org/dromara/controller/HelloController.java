package org.dromara.controller;

import org.dromara.service.IChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
