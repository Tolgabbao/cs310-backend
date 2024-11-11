package com.howudoin.cs310backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


/**
 * Filter that intercepts incoming HTTP requests to extract and validate JWT tokens.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService customUserDetailsService;

    /**
     * Filters each request to check for JWT token and authenticate the user.
     *
     * @param request     Incoming HTTP request.
     * @param response    HTTP response.
     * @param filterChain Filter chain.
     * @throws ServletException In case of servlet errors.
     * @throws IOException      In case of I/O errors.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            // Extract JWT token from the Authorization header
            String jwt = parseJwt(request);
            if (jwt != null && jwtUtil.validateJwt(jwt)) {
                // Extract user ID from the token
                String userId = jwtUtil.getUserIdFromJwt(jwt);

                // Load user details associated with the user ID
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(userId);

                // Create authentication token
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities());

                // Set authentication in the security context
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            // Log the exception (use a logger instead of printStackTrace)
            e.printStackTrace();
        }

        // Continue with the filter chain
        filterChain.doFilter(request, response);
    }

    /**
     * Parses the JWT token from the Authorization header.
     *
     * @param request HTTP request.
     * @return JWT token if present, else null.
     */
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        // Check if the Authorization header is present and starts with "Bearer "
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            // Extract and return the token
            return headerAuth.substring(7);
        }

        return null;
    }
}
