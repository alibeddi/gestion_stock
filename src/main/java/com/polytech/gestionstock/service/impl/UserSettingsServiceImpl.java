package com.polytech.gestionstock.service.impl;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.polytech.gestionstock.exception.EntityNotFoundException;
import com.polytech.gestionstock.model.dto.UserSettingsDto;
import com.polytech.gestionstock.model.entity.User;
import com.polytech.gestionstock.model.entity.settings.UserSettings;
import com.polytech.gestionstock.repository.UserRepository;
import com.polytech.gestionstock.repository.UserSettingsRepository;
import com.polytech.gestionstock.service.UserSettingsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserSettingsServiceImpl implements UserSettingsService {

    private final UserSettingsRepository userSettingsRepository;
    private final UserRepository userRepository;

    @Override
    public UserSettingsDto getSettingsByUserId(Long userId) {
        log.info("Fetching user settings for user ID: {}", userId);
        
        Optional<UserSettings> userSettingsOpt = userSettingsRepository.findByUserId(userId);
        
        if (userSettingsOpt.isPresent()) {
            return mapToDto(userSettingsOpt.get());
        } else {
            log.info("No settings found for user ID: {}, returning null", userId);
            return null;
        }
    }

    @Override
    public UserSettingsDto saveUserSettings(UserSettingsDto userSettingsDto) {
        log.info("Saving user settings: {}", userSettingsDto);
        
        // Check if user exists
        User user = userRepository.findById(userSettingsDto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User", "id", userSettingsDto.getUserId()));
        
        // Check if settings already exist for this user
        Optional<UserSettings> existingSettings = userSettingsRepository.findByUserId(user.getId());
        
        UserSettings userSettings;
        if (existingSettings.isPresent()) {
            // Update existing settings
            userSettings = existingSettings.get();
            userSettings.setLanguage(userSettingsDto.getLanguage());
            userSettings.setNotificationEmail(userSettingsDto.getNotificationEmail());
            userSettings.setNotificationSms(userSettingsDto.getNotificationSms());
        } else {
            // Create new settings
            userSettings = mapToEntity(userSettingsDto);
            userSettings.setUser(user);
        }
        
        UserSettings savedUserSettings = userSettingsRepository.save(userSettings);
        
        return mapToDto(savedUserSettings);
    }

    @Override
    public UserSettingsDto updateUserSettings(Long userId, UserSettingsDto userSettingsDto) {
        log.info("Updating user settings for user ID: {}", userId);
        
        // Check if user exists
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User", "id", userId));
        
        // Check if settings exist, create if they don't
        UserSettings existingSettings = userSettingsRepository.findByUserId(userId)
                .orElseGet(() -> {
                    UserSettings newSettings = new UserSettings();
                    newSettings.setUser(user);
                    return newSettings;
                });
        
        // Update fields
        existingSettings.setLanguage(userSettingsDto.getLanguage());
        existingSettings.setNotificationEmail(userSettingsDto.getNotificationEmail());
        existingSettings.setNotificationSms(userSettingsDto.getNotificationSms());
        
        UserSettings updatedSettings = userSettingsRepository.save(existingSettings);
        
        return mapToDto(updatedSettings);
    }

    @Override
    public UserSettingsDto getSettingsByCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();
        
        User currentUser = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new EntityNotFoundException("User", "email", currentUserEmail));
        
        return getSettingsByUserId(currentUser.getId());
    }
    
    // Helper methods for mapping between entity and DTO
    private UserSettingsDto mapToDto(UserSettings userSettings) {
        return UserSettingsDto.builder()
                .id(userSettings.getId())
                .userId(userSettings.getUser().getId())
                .language(userSettings.getLanguage())
                .notificationEmail(userSettings.getNotificationEmail())
                .notificationSms(userSettings.getNotificationSms())
                .dateCreation(userSettings.getDateCreation())
                .dateModification(userSettings.getDateModification())
                .build();
    }
    
    private UserSettings mapToEntity(UserSettingsDto dto) {
        UserSettings userSettings = new UserSettings();
        userSettings.setLanguage(dto.getLanguage());
        userSettings.setNotificationEmail(dto.getNotificationEmail());
        userSettings.setNotificationSms(dto.getNotificationSms());
        
        return userSettings;
    }
} 