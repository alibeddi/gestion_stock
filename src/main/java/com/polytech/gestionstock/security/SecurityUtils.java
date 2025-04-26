package com.polytech.gestionstock.security;

import com.polytech.gestionstock.model.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Utility class for Security related functionality
 */
@Component
public class SecurityUtils {

    /**
     * Checks if the current authenticated user is the same as the requested user ID
     * 
     * @param userId The ID of the user being accessed
     * @return true if the authenticated user is accessing their own data
     */
    public boolean isCurrentUser(Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        
        Object principal = authentication.getPrincipal();
        
        if (!(principal instanceof User)) {
            return false;
        }
        
        User user = (User) principal;
        return user.getId().equals(userId);
    }

    /**
     * Gets the currently authenticated user
     * 
     * @return The authenticated user or null if not authenticated
     */
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        
        Object principal = authentication.getPrincipal();
        
        if (!(principal instanceof User)) {
            return null;
        }
        
        return (User) principal;
    }
} 