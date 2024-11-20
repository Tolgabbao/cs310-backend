// src/main/java/com/howudoin/model/User.java

package com.howudoin.cs310backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private String passwordHash;
    @JsonIgnore
    private List<String> friendList;
    @JsonIgnore
    private List<String> pendingRequests;
    @JsonIgnore
    private List<String> sentRequests;
    private Long createdAt;
    @JsonIgnore
    private Long updatedAt;

}
