package com.polytech.gestionstock.service.impl;

import com.polytech.gestionstock.service.LogoutService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogoutServiceImpl implements LogoutService {

    // In-memory set of invalidated tokens (should be replaced with Redis or similar in production)
    private static final Set<String> invalidatedTokens = new HashSet<>();

    @Override
    public void logout(String token) {
        log.info("Logging out user and invalidating token");
        
        // Add token to invalidated tokens set
        if (token != null && !token.isEmpty()) {
            invalidatedTokens.add(token);
            log.debug("Token invalidated successfully");
        }
        
        // Clear the security context
        SecurityContextHolder.clearContext();
    }
    
    /**
     * Checks if a token has been invalidated (logged out)
     * 
     * @param token JWT token to check
     * @return true if the token is invalid (user has logged out)
     */
    public boolean isTokenInvalidated(String token) {
        return invalidatedTokens.contains(token);
    }
} 