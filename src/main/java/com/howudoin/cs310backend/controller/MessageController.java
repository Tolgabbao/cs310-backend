// src/main/java/com/howudoin/controller/MessageController.java

package com.howudoin.cs310backend.controller;

import com.howudoin.cs310backend.model.Message;
import com.howudoin.cs310backend.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
// Import statements
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@RestController
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@Valid @RequestBody SendMessageRequest request, Authentication authentication) {
        try {
            String senderId = authentication.getName();
            // only can send direct messages to friends
            if (!messageService.areFriends(senderId, request.getRecipientId())) {
                return ResponseEntity.badRequest().body("You can only send messages to friends.");
            }
            Message message = messageService.sendMessage(senderId, request.getRecipientId(), request.getContent(), "DIRECT");
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getConversation(@RequestParam String friendId, Authentication authentication) {
        try {
            String userId = authentication.getName();
            List<Message> messages = messageService.getConversation(userId, friendId);
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

@Data
class SendMessageRequest {
    @NotBlank
    private String recipientId;

    @NotBlank
    private String content;
}
