package com.polytech.gestionstock.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.polytech.gestionstock.model.dto.CompanySettingsDto;
import com.polytech.gestionstock.model.dto.UserSettingsDto;
import com.polytech.gestionstock.model.response.ApiResponse;
import com.polytech.gestionstock.service.CompanySettingsService;
import com.polytech.gestionstock.service.UserSettingsService;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/settings")
@RequiredArgsConstructor
@Slf4j
public class SettingsController {

    private final UserSettingsService userSettingsService;
    private final CompanySettingsService companySettingsService;
    
    @GetMapping
    public ResponseEntity<ApiResponse<AllSettings>> getAllSettings() {
        log.info("Fetching all settings (user + company)");
        
        UserSettingsDto userSettings = null;
        try {
            userSettings = userSettingsService.getSettingsByCurrentUser();
        } catch (Exception e) {
            log.warn("Error fetching user settings: {}", e.getMessage());
        }
        
        CompanySettingsDto companySettings = null;
        try {
            companySettings = companySettingsService.getCompanySettings();
        } catch (Exception e) {
            log.warn("Error fetching company settings: {}", e.getMessage());
        }
        
        AllSettings allSettings = new AllSettings(userSettings, companySettings);
        
        return ResponseEntity.ok(ApiResponse.success(allSettings, "Settings retrieved successfully"));
    }
    
    @Data
    public static class AllSettings {
        private final UserSettingsDto userSettings;
        private final CompanySettingsDto companySettings;
    }
} 