package org.dromara.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.dromara.bean.ChatEntity;
import org.dromara.service.IChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lee
 * @description
 */
@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private IChatService chatService;

    @PostMapping("/deepSeek/doChat")
    public void deepSeekChat(@RequestBody ChatEntity chatEntity) {
        System.out.println("ChatEntity: " + chatEntity);
        chatService.deepSeekChat(chatEntity);
    }

}
