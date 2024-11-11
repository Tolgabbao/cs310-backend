// src/main/java/com/howudoin/controller/GroupController.java

package com.howudoin.cs310backend.controller;

import com.howudoin.cs310backend.model.Group;
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

    @GetMapping("/{groupId}/members")
    public ResponseEntity<?> getGroupMembers(@PathVariable String groupId, Authentication authentication) {
        try {
            List<String> members = groupService.getGroupMembers(groupId);
            return ResponseEntity.ok(members);
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
