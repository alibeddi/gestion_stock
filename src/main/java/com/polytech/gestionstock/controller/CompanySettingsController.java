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

import com.polytech.gestionstock.model.dto.CompanySettingsDto;
import com.polytech.gestionstock.model.response.ApiResponse;
import com.polytech.gestionstock.service.CompanySettingsService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/settings/company")
@RequiredArgsConstructor
@Slf4j
public class CompanySettingsController {

    private final CompanySettingsService companySettingsService;
    
    @GetMapping
    public ResponseEntity<ApiResponse<CompanySettingsDto>> getCompanySettings() {
        log.info("Fetching company settings");
        
        CompanySettingsDto settings = companySettingsService.getCompanySettings();
        String message = settings != null ? 
                "Company settings retrieved successfully" : 
                "No company settings found";
        
        return ResponseEntity.ok(ApiResponse.success(settings, message));
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CompanySettingsDto>> createCompanySettings(@Valid @RequestBody CompanySettingsDto companySettingsDto) {
        log.info("Creating company settings: {}", companySettingsDto);
        
        CompanySettingsDto createdSettings = companySettingsService.saveCompanySettings(companySettingsDto);
        
        return ResponseEntity.ok(ApiResponse.success(createdSettings, "Company settings created successfully"));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CompanySettingsDto>> updateCompanySettings(
            @PathVariable Long id,
            @Valid @RequestBody CompanySettingsDto companySettingsDto) {
        log.info("Updating company settings with ID: {}", id);
        
        CompanySettingsDto updatedSettings = companySettingsService.updateCompanySettings(id, companySettingsDto);
        
        return ResponseEntity.ok(ApiResponse.success(updatedSettings, "Company settings updated successfully"));
    }
} 