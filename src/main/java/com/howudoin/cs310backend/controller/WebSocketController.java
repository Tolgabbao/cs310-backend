// src/main/java/com/howudoin/controller/WebSocketController.java

package com.howudoin.cs310backend.controller;

import com.howudoin.cs310backend.model.Message;
import com.howudoin.cs310backend.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
// Import statements
import org.springframework.stereotype.Controller;

import lombok.Data;

@Controller
public class WebSocketController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private MessageService messageService;

    @MessageMapping("/send")
    public void sendMessage(@Payload SendMessageRequest request, Authentication authentication) throws Exception {
        String senderId = authentication.getName();
        Message message = messageService.sendMessage(senderId, request.getRecipientId(), request.getContent(), "TEXT");

        // Send to recipient's queue
        messagingTemplate.convertAndSend("/queue/messages/" + request.getRecipientId(), message);

        // Optionally, send to sender's own queue for confirmation
        messagingTemplate.convertAndSend("/queue/messages/" + senderId, message);
    }
}


