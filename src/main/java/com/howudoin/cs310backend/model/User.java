// src/main/java/com/howudoin/model/User.java

package com.howudoin.cs310backend.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.UUID;

@Document(collection = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    private String userId = UUID.randomUUID().toString();

    private String firstName;
    private String lastName;
    private String email;
    private String passwordHash;
    private List<String> friendList;
    private List<String> pendingRequests;
    private List<String> sentRequests;
    private Long createdAt;
    private Long updatedAt;

}
