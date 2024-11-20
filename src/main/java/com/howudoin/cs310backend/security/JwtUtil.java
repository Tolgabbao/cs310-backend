package com.howudoin.cs310backend.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Base64;
import java.security.Key;

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
        byte[] keyBytes = Base64.getDecoder().decode(jwtSecret);
        Key signingKey = Keys.hmacShaKeyFor(keyBytes);

        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(signingKey)
                .compact();
    }


    /**
     * Extracts the user ID from a JWT token.
     *
     * @param token JWT token.
     * @return User ID.
     */
    public String getUserIdFromJwt(String token) {
        try {
            // Ensure jwtSecret is Base64-encoded for the signing key
            byte[] keyBytes = Base64.getDecoder().decode(jwtSecret);
            Key signingKey = Keys.hmacShaKeyFor(keyBytes);

            // Parse the JWT token and retrieve claims
            Claims claims = Jwts.parser()
                    .setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getSubject(); // Extract the 'sub' field (userId)
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Validates a JWT token.
     *
     * @param authToken JWT token.
     * @return True if valid, else false.
     */
    public boolean validateJwt(String authToken) {
        try {
            if (authToken == null || !authToken.contains(".")) {
                System.out.println("Invalid JWT format: " + authToken); // Debugging line
                return false;
            }
            Jwts.parser()
                    .setSigningKey(Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtSecret)))
                    .build()
                    .parseClaimsJws(authToken); // Attempt to parse
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            e.printStackTrace();
            return false; // Token is invalid
        }
    }

}
