package com.polytech.gestionstock.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.polytech.gestionstock.model.dto.UserSettingsDto;
import com.polytech.gestionstock.model.response.ApiResponse;
import com.polytech.gestionstock.service.UserSettingsService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/settings/user")
@RequiredArgsConstructor
@Slf4j
public class UserSettingsController {

    private final UserSettingsService userSettingsService;
    
    @GetMapping("/current")
    public ResponseEntity<ApiResponse<UserSettingsDto>> getCurrentUserSettings() {
        log.info("Fetching settings for current user");
        
        UserSettingsDto settings = userSettingsService.getSettingsByCurrentUser();
        String message = settings != null ? 
                "Current user settings retrieved successfully" : 
                "No settings found for current user";
        
        return ResponseEntity.ok(ApiResponse.success(settings, message));
    }
    
    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN') or @securityUtils.isCurrentUser(#userId)")
    public ResponseEntity<ApiResponse<UserSettingsDto>> getUserSettings(@PathVariable Long userId) {
        log.info("Fetching settings for user ID: {}", userId);
        
        UserSettingsDto settings = userSettingsService.getSettingsByUserId(userId);
        String message = settings != null ? 
                "User settings retrieved successfully" : 
                "No settings found for user ID: " + userId;
        
        return ResponseEntity.ok(ApiResponse.success(settings, message));
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<UserSettingsDto>> createUserSettings(@Valid @RequestBody UserSettingsDto userSettingsDto) {
        log.info("Creating user settings: {}", userSettingsDto);
        
        UserSettingsDto createdSettings = userSettingsService.saveUserSettings(userSettingsDto);
        
        return ResponseEntity.ok(ApiResponse.success(createdSettings, "User settings created successfully"));
    }
    
    @PutMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN') or @securityUtils.isCurrentUser(#userId)")
    public ResponseEntity<ApiResponse<UserSettingsDto>> updateUserSettings(
            @PathVariable Long userId,
            @Valid @RequestBody UserSettingsDto userSettingsDto) {
        log.info("Updating settings for user ID: {}", userId);
        
        UserSettingsDto updatedSettings = userSettingsService.updateUserSettings(userId, userSettingsDto);
        
        return ResponseEntity.ok(ApiResponse.success(updatedSettings, "User settings updated successfully"));
    }
} 