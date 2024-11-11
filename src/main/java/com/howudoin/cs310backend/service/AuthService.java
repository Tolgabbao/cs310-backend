// src/main/java/com/howudoin/service/AuthService.java

package com.howudoin.cs310backend.service;

import com.howudoin.cs310backend.model.User;
import com.howudoin.cs310backend.security.JwtUtil;
import com.howudoin.cs310backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
// Import statements
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    public String authenticateUser(String email, String password) throws Exception {
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new Exception("Invalid email or password."));

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUserId(), password)
        );

        return jwtUtil.generateToken(user.getUserId());
    }
}
