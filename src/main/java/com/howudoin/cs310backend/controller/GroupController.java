// src/main/java/com/howudoin/controller/GroupController.java

package com.howudoin.cs310backend.controller;

import com.howudoin.cs310backend.model.Group;
import com.howudoin.cs310backend.model.Message;
import com.howudoin.cs310backend.model.User;
import com.howudoin.cs310backend.service.GroupService;
import com.howudoin.cs310backend.service.UserService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
// Import statements
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/groups")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<?> createGroup(@Valid @RequestBody CreateGroupRequest request, Authentication authentication) {
        try {
            String userId = authentication.getName();
            Group group = groupService.createGroup(request.getGroupName(), userId, request.getMemberIds());
            return ResponseEntity.status(201).body(group);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{groupId}/add-member")
    public ResponseEntity<?> addMember(@PathVariable String groupId, @Valid @RequestBody AddMemberRequest request, Authentication authentication) {
        try {
            String adminId = authentication.getName();
            groupService.addMember(groupId, request.getMemberId(), adminId);
            return ResponseEntity.ok("Member added to group.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // send a message to a group
    @PostMapping("/{groupId}/send")
    public ResponseEntity<?> sendMessage(@PathVariable String groupId, @Valid @RequestBody SendMessageRequest request, Authentication authentication) {
        try {
            String senderId = authentication.getName();
            groupService.sendMessage(groupId, senderId, request.getContent());
            return ResponseEntity.ok("Message sent to group.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{groupId}/members")
    public ResponseEntity<?> getGroupMembers(@PathVariable String groupId, Authentication authentication) {
        try {
            List<String> members = groupService.getGroupMembers(groupId);
            return ResponseEntity.ok(members);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("{groupId}/messages")
    public ResponseEntity<?> getGroupMessages(@PathVariable String groupId, Authentication authentication) {
        try {
            List<Message> messages = groupService.getGroupMessages(groupId);
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getGroups(Authentication authentication) {
        try {
            String userId = authentication.getName();
            List<Group> groups = groupService.getGroups(userId);
            return ResponseEntity.ok(groups);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/details/{groupId}")
    public ResponseEntity<?> getGroupDetails(@PathVariable String groupId, Authentication authentication) {
        try {
            Group group = groupService.getGroupById(groupId);
            // parse the group members to get their details
            List<String> memberIds = group.getMembers();
            List<Optional<User>> members = memberIds.stream().map(s -> userService.findById(s)).toList();
            String adminId = group.getCreatedBy();
            String adminName = userService.findById(adminId).get().getFirstName() + " " + userService.findById(adminId).get().getLastName();
            return ResponseEntity.ok(new GroupDetailsResponse(group, members, adminName));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

@Data
class CreateGroupRequest {
    @NotBlank
    private String groupName;

    private List<String> memberIds;
}

@Data
class AddMemberRequest {
    @NotBlank
    private String memberId;
}

@Data
class GroupDetailsResponse {
    private Group group;
    private List<Optional<User>> members;
    private String adminName;

    public GroupDetailsResponse(Group group, List<Optional<User>> members, String adminName) {
        this.group = group;
        this.members = members;
        this.adminName = adminName;
    }
}
