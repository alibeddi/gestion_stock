package com.polytech.gestionstock.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.polytech.gestionstock.exception.EntityNotFoundException;
import com.polytech.gestionstock.model.dto.CompanySettingsDto;
import com.polytech.gestionstock.model.entity.settings.CompanySettings;
import com.polytech.gestionstock.repository.CompanySettingsRepository;
import com.polytech.gestionstock.service.CompanySettingsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CompanySettingsServiceImpl implements CompanySettingsService {

    private final CompanySettingsRepository companySettingsRepository;

    @Override
    public CompanySettingsDto getCompanySettings() {
        log.info("Fetching company settings");
        
        // Get the first company settings record, or create a default one if none exists
        List<CompanySettings> settings = companySettingsRepository.findAll();
        CompanySettings companySettings;
        
        if (settings.isEmpty()) {
            log.info("No company settings found, returning empty settings object");
            // Instead of creating default settings with fake data, return null
            // so the frontend can handle the initial setup
            return null;
        } else {
            companySettings = settings.get(0);
            return mapToDto(companySettings);
        }
    }

    @Override
    public CompanySettingsDto saveCompanySettings(CompanySettingsDto companySettingsDto) {
        log.info("Saving company settings: {}", companySettingsDto);
        
        CompanySettings companySettings = mapToEntity(companySettingsDto);
        CompanySettings savedSettings = companySettingsRepository.save(companySettings);
        
        return mapToDto(savedSettings);
    }

    @Override
    public CompanySettingsDto updateCompanySettings(Long id, CompanySettingsDto companySettingsDto) {
        log.info("Updating company settings with ID: {}", id);
        
        CompanySettings existingSettings = companySettingsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("CompanySettings", "id", id));
        
        // Update fields
        updateEntityFromDto(existingSettings, companySettingsDto);
        
        CompanySettings updatedSettings = companySettingsRepository.save(existingSettings);
        
        return mapToDto(updatedSettings);
    }
    
    // Helper methods
    
    private CompanySettingsDto mapToDto(CompanySettings companySettings) {
        return CompanySettingsDto.builder()
                .id(companySettings.getId())
                .companyName(companySettings.getCompanyName())
                .taxId(companySettings.getTaxId())
                .email(companySettings.getEmail())
                .phone(companySettings.getPhone())
                .address(companySettings.getAddress())
                .postalCode(companySettings.getPostalCode())
                .city(companySettings.getCity())
                .country(companySettings.getCountry())
                .website(companySettings.getWebsite())
                .logoUrl(companySettings.getLogoUrl())
                .currency(companySettings.getCurrency())
                .dateCreation(companySettings.getDateCreation())
                .dateModification(companySettings.getDateModification())
                .build();
    }
    
    private CompanySettings mapToEntity(CompanySettingsDto dto) {
        CompanySettings companySettings = new CompanySettings();
        updateEntityFromDto(companySettings, dto);
        return companySettings;
    }
    
    private void updateEntityFromDto(CompanySettings entity, CompanySettingsDto dto) {
        entity.setCompanyName(dto.getCompanyName());
        entity.setTaxId(dto.getTaxId());
        entity.setEmail(dto.getEmail());
        entity.setPhone(dto.getPhone());
        entity.setAddress(dto.getAddress());
        entity.setPostalCode(dto.getPostalCode());
        entity.setCity(dto.getCity());
        entity.setCountry(dto.getCountry());
        entity.setWebsite(dto.getWebsite());
        entity.setLogoUrl(dto.getLogoUrl());
        entity.setCurrency(dto.getCurrency());
    }
} 