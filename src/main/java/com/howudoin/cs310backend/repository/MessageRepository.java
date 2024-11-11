// src/main/java/com/howudoin/repository/MessageRepository.java

package com.howudoin.cs310backend.repository;

import com.howudoin.cs310backend.model.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends MongoRepository<Message, String> {
    List<Message> findByReceiverIdAndSenderIdOrderByTimestampAsc(String userId, String friendId);

    List<Message> findByReceiverIdOrderByTimestampAsc(String groupId);
}
