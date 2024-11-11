// src/main/java/com/howudoin/repository/GroupRepository.java

package com.howudoin.cs310backend.repository;

import com.howudoin.cs310backend.model.Group;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends MongoRepository<Group, String> {
    // Additional query methods if needed
}
