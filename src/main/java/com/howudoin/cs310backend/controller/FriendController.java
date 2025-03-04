package com.howudoin.cs310backend.controller;

import com.howudoin.cs310backend.model.User;
import com.howudoin.cs310backend.service.FriendService;
import com.howudoin.cs310backend.service.UserService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/friends")
public class FriendController {

    @Autowired
    private FriendService friendService;

    @Autowired
    private UserService userService;

    @PostMapping("/add")
    public ResponseEntity<?> sendFriendRequest(@Valid @RequestBody FriendRequest request, Authentication authentication) {
        try {
            String userId = authentication.getName();
            Optional<User> friend = userService.findByEmail(request.getEmail());
            // print to console debug
            System.out.println("FriendController: sendFriendRequest: friend: " + friend);
            if (friend == null) {
                return ResponseEntity.badRequest().body("User with the provided email not found.");
            }
            friendService.sendFriendRequest(userId, friend.get().getUserId());
            return ResponseEntity.ok("Friend request sent.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/accept")
    public ResponseEntity<?> acceptFriendRequest(@Valid @RequestBody FriendRequest request, Authentication authentication) {
        try {
            String userId = authentication.getName();
            Optional<User> friend = userService.findByEmail(request.getEmail());
            // print to console debug
            System.out.println("FriendController.java: acceptFriendRequest: friend: " + friend);
            if (friend == null) {
                return ResponseEntity.badRequest().body("User with the provided email not found.");
            }
            friendService.acceptFriendRequest(userId, friend.get().getUserId());
            return ResponseEntity.ok("Friend request accepted.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/pending")
    public ResponseEntity<?> getPendingFriendRequests(Authentication authentication) {
        try {
            String userId = authentication.getName();
            List<User> pendingRequests = friendService.getPendingFriendRequests(userId);
            // return the list of emails of users who have sent friend requests to the users
            // the pendingRequests list should only have the email field but its type is User
            return ResponseEntity.ok(pendingRequests);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/sent")
    public ResponseEntity<?> getSentFriendRequests(Authentication authentication) {
        try {
            String userId = authentication.getName();
            List<User> sentRequests = friendService.getSentFriendRequests(userId);
            // return the list of emails of users to whom the users have sent friend requests
            // the sentRequests list should only have the email field but its type is User
            return ResponseEntity.ok(sentRequests);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getFriendList(Authentication authentication) {
        try {
            System.out.println("FriendController: getFriendList: authentication: " + authentication);
            String userId = authentication.getName();
            List<User> friends = friendService.getFriends(userId);
            // only should have most of friends data in response

            return ResponseEntity.ok(friends);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

@Data
class FriendRequest {
    @NotBlank
    @Email
    private String email;
}