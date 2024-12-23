// src/main/java/com/howudoin/service/FriendService.java

package com.howudoin.cs310backend.service;

import com.howudoin.cs310backend.model.User;
import com.howudoin.cs310backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FriendService {

    @Autowired
    private UserRepository userRepository;

    public void sendFriendRequest(String fromUserId, String toUserId) throws Exception {
        if (fromUserId.equals(toUserId)) {
            throw new Exception("Cannot send friend request to yourself.");
        }

        User fromUser = userRepository.findById(fromUserId)
                .orElseThrow(() -> new Exception("User not found."));
        User toUser = userRepository.findById(toUserId)
                .orElseThrow(() -> new Exception("User not found."));

        if (fromUser.getFriendList().contains(toUserId)) {
            throw new Exception("Users are already friends.");
        }

        if (fromUser.getSentRequests().contains(toUserId)) {
            throw new Exception("Friend request already sent.");
        }

        if (toUser.getPendingRequests().contains(fromUserId)) {
            throw new Exception("Friend request already received.");
        }

        fromUser.getSentRequests().add(toUserId);
        toUser.getPendingRequests().add(fromUserId);

        userRepository.save(fromUser);
        userRepository.save(toUser);
    }

    public void acceptFriendRequest(String userId, String friendId) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("User not found."));
        User friend = userRepository.findById(friendId)
                .orElseThrow(() -> new Exception("Friend not found."));

        if (!user.getPendingRequests().contains(friendId)) {
            throw new Exception("No pending friend request from this user.");
        }

        user.getPendingRequests().remove(friendId);
        friend.getSentRequests().remove(userId);

        user.getFriendList().add(friendId);
        friend.getFriendList().add(userId);

        userRepository.save(user);
        userRepository.save(friend);
    }
    

    public List<User> getFriends(String userId) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("User not found."));
        List<String> friendIds = user.getFriendList();
        return userRepository.findAllById(friendIds);
    }

    public List<User> getPendingFriendRequests(String userId) {
        User user = userRepository.findById(userId).orElseThrow();
        List<String> pendingRequests = user.getPendingRequests();
        return userRepository.findAllById(pendingRequests);
    }
}
