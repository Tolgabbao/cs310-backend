// src/main/java/com/howudoin/model/Message.java

package com.howudoin.cs310backend.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document(collection = "messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {
    @Id
    private String messageId = UUID.randomUUID().toString();

    private String senderId;
    private String receiverId; // userId or groupId
    private String content;
    private Long timestamp;
    private String messageType; // e.g., "TEXT", "IMAGE", etc.
    private String status; // e.g., "SENT", "DELIVERED", "READ"
}
