package com.polytech.gestionstock.service;

import com.polytech.gestionstock.model.dto.EmballageDto;

import java.util.List;

public interface EmballageService {
    EmballageDto save(EmballageDto emballageDto);
    EmballageDto update(Long id, EmballageDto emballageDto);
    EmballageDto findById(Long id);
    EmballageDto findByCode(String code);
    List<EmballageDto> findByLibelle(String libelle);
    List<EmballageDto> findByTypeEmballage(String typeEmballage);
    List<EmballageDto> findAll();
    void delete(Long id);
} 