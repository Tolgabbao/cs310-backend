package com.howudoin.cs310backend.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Utility class for handling JWT operations like token generation and validation.
 */
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expirationMs}")
    private int jwtExpirationMs;

    /**
     * Generates a JWT token for a given user ID.
     *
     * @param userId User ID.
     * @return Generated JWT token.
     */
    public String generateToken(String userId) {
        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    /**
     * Extracts the user ID from a JWT token.
     *
     * @param token JWT token.
     * @return User ID.
     */
    public String getUserIdFromJwt(String token) {
        return ""; // implement later
    }

    /**
     * Validates a JWT token.
     *
     * @param authToken JWT token.
     * @return True if valid, else false.
     */
    public boolean validateJwt(String authToken) {
        try {
            // implement later
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // Log the exception (use a logger in production)
            e.printStackTrace();
        }
        return false;
    }
}
