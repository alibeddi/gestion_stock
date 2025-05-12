package com.polytech.gestionstock.service;

import com.polytech.gestionstock.model.dto.CompanySettingsDto;

public interface CompanySettingsService {
    CompanySettingsDto getCompanySettings();
    CompanySettingsDto saveCompanySettings(CompanySettingsDto companySettingsDto);
    CompanySettingsDto updateCompanySettings(Long id, CompanySettingsDto companySettingsDto);
} 