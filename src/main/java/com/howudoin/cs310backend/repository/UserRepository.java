// src/main/java/com/howudoin/repository/UserRepository.java

package com.howudoin.cs310backend.repository;

import com.howudoin.cs310backend.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByUserId(String userId);
    Optional<User> findByUserId(String userId);

}
