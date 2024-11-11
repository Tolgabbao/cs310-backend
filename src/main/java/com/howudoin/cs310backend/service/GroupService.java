// src/main/java/com/howudoin/service/GroupService.java

package com.howudoin.cs310backend.service;

import com.howudoin.cs310backend.model.Group;
import com.howudoin.cs310backend.model.User;
import com.howudoin.cs310backend.repository.GroupRepository;
import com.howudoin.cs310backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    public Group createGroup(String groupName, String createdBy, List<String> memberIds) throws Exception {
        // Validate members
        for (String memberId : memberIds) {
            if (!userRepository.existsById(memberId)) {
                throw new Exception("User with ID " + memberId + " does not exist.");
            }
        }

        // Ensure creator is part of the group
        if (!memberIds.contains(createdBy)) {
            memberIds.add(createdBy);
        }

        Group group = Group.builder()
                .groupName(groupName)
                .createdBy(createdBy)
                .members(memberIds)
                .createdAt(System.currentTimeMillis())
                .updatedAt(System.currentTimeMillis())
                .build();
        return groupRepository.save(group);
    }

    public void addMember(String groupId, String userId, String adminId) throws Exception {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new Exception("Group not found."));

        if (!group.getCreatedBy().equals(adminId)) {
            throw new Exception("Only group admin can add members.");
        }

        if (!userRepository.existsById(userId)) {
            throw new Exception("User not found.");
        }

        if (group.getMembers().contains(userId)) {
            throw new Exception("User is already a member of the group.");
        }

        group.getMembers().add(userId);
        group.setUpdatedAt(System.currentTimeMillis());
        groupRepository.save(group);
    }

    public List<String> getGroupMembers(String groupId) throws Exception {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new Exception("Group not found."));
        return group.getMembers();
    }
}
