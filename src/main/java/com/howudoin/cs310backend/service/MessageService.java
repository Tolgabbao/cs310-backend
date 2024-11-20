// src/main/java/com/howudoin/service/MessageService.java

package com.howudoin.cs310backend.service;

import com.howudoin.cs310backend.model.Message;
import com.howudoin.cs310backend.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public Message sendMessage(String senderId, String receiverId, String content, String messageType) {
        Message message = Message.builder()
                .senderId(senderId)
                .receiverId(receiverId)
                .content(content)
                .messageType(messageType)
                .timestamp(System.currentTimeMillis())
                .status("SENT")
                .build();
        return messageRepository.save(message);
    }

    public List<Message> getConversation(String userId, String friendId) {
        // Find messages from both directions
        System.out.println("Looking up messages between " + userId + " and " + friendId);
        List<Message> messages1 = messageRepository.findByReceiverIdAndSenderIdOrderByTimestampAsc(userId, friendId);
        List<Message> messages2 = messageRepository.findByReceiverIdAndSenderIdOrderByTimestampAsc(friendId, userId);
        messages1.addAll(messages2);
        messages1.sort((m1, m2) -> m1.getTimestamp().compareTo(m2.getTimestamp()));
        System.out.println("Found " + messages1.size() + " messages between " + userId + " and " + friendId);
        return messages1;
    }

    public List<Message> getGroupMessages(String groupId) {
        // Implement pagination as needed
        return messageRepository.findByReceiverIdOrderByTimestampAsc(groupId);
    }
}
