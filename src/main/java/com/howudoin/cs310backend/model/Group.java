// src/main/java/com/howudoin/model/Group.java

package com.howudoin.cs310backend.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import java.util.List;
import java.util.UUID;

@Document(collection = "groups")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Group {
    @Id
    private String groupId = UUID.randomUUID().toString();

    private String groupName;
    private String createdBy; // userId of the admin
    private List<String> members; // list of userIds
    private Long createdAt;
    private Long updatedAt;
    private List<Message> messages;
}
