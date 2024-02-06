package com.example.coffee.controllers;

import com.example.coffee.entity.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.handler.annotation.Payload;

public class RealTimeChat {

    private final SimpMessagingTemplate messagingTemplate;

    public RealTimeChat(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }


    @MessageMapping("/message")
    @SendTo("/group/public")
    public void receiveMessage(@Payload Message message) {
        messagingTemplate.convertAndSend("/group/" + message.getReceiver().getId().toString(), message);
    }
}
