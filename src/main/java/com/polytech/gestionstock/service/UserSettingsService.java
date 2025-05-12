package com.polytech.gestionstock.service;

import com.polytech.gestionstock.model.dto.UserSettingsDto;

public interface UserSettingsService {
    UserSettingsDto getSettingsByUserId(Long userId);
    UserSettingsDto saveUserSettings(UserSettingsDto userSettingsDto);
    UserSettingsDto updateUserSettings(Long userId, UserSettingsDto userSettingsDto);
    UserSettingsDto getSettingsByCurrentUser();
} 