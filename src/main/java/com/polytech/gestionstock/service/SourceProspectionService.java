package com.polytech.gestionstock.service;

import com.polytech.gestionstock.model.dto.SourceProspectionDto;

import java.util.List;

public interface SourceProspectionService {
    SourceProspectionDto save(SourceProspectionDto sourceProspectionDto);
    SourceProspectionDto update(Long id, SourceProspectionDto sourceProspectionDto);
    SourceProspectionDto findById(Long id);
    SourceProspectionDto findByCode(String code);
    SourceProspectionDto findByLibelle(String libelle);
    List<SourceProspectionDto> findAll();
    void delete(Long id);
} 