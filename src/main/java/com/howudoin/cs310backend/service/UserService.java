// src/main/java/com/howudoin/service/UserService.java

package com.howudoin.cs310backend.service;

import com.howudoin.cs310backend.model.User;
import com.howudoin.cs310backend.repository.UserRepository;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// Import statements
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Configuration
class PasswordEncoderConfig {
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public User registerUser(String firstName, String lastName, String email, String password) throws Exception {
        if (userRepository.existsByEmail(email)) {
            throw new Exception("Email already in use.");
        }
        User user = User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .passwordHash(passwordEncoder.encode(password))
                .friendList(new ArrayList<>())
                .pendingRequests(new ArrayList<>())
                .sentRequests(new ArrayList<>())
                .createdAt(System.currentTimeMillis())
                .updatedAt(System.currentTimeMillis())
                .build();
        return userRepository.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findById(String userId) {
        return userRepository.findById(userId);
    }

    public boolean areFriends(String senderId, @NotBlank String recipientId) {
        Optional<User> sender = userRepository.findById(senderId);
        Optional<User> recipient = userRepository.findById(recipientId);
        if (sender.isPresent() && recipient.isPresent()) {
            return sender.get().getFriendList().contains(recipientId) && recipient.get().getFriendList().contains(senderId);
        }
        return false;
    }

    // Additional user-related methods
}
