package com.polytech.gestionstock.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.polytech.gestionstock.exception.DuplicateEntityException;
import com.polytech.gestionstock.exception.EntityNotFoundException;
import com.polytech.gestionstock.model.dto.EmballageDto;
import com.polytech.gestionstock.model.entity.Emballage;
import com.polytech.gestionstock.repository.EmballageRepository;
import com.polytech.gestionstock.service.EmballageService;
import com.polytech.gestionstock.util.ObjectMapperUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmballageServiceImpl implements EmballageService {

    private final EmballageRepository emballageRepository;

    @Override
    @Transactional
    public EmballageDto save(EmballageDto emballageDto) {
        log.info("Saving emballage with code: {}", emballageDto.getCode());
        
        if (emballageRepository.existsByCode(emballageDto.getCode())) {
            throw new DuplicateEntityException("Emballage", "code", emballageDto.getCode());
        }
        
        Emballage emballage = ObjectMapperUtils.mapToEntity(emballageDto, Emballage.class);
        emballage = emballageRepository.save(emballage);
        
        return ObjectMapperUtils.mapToDto(emballage, EmballageDto.class);
    }

    @Override
    @Transactional
    public EmballageDto update(Long id, EmballageDto emballageDto) {
        log.info("Updating emballage with ID: {}", id);
        
        Emballage existingEmballage = emballageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Emballage", "id", id));
        
        // Check if code is being changed and if it already exists
        if (!existingEmballage.getCode().equals(emballageDto.getCode()) && 
                emballageRepository.existsByCode(emballageDto.getCode())) {
            throw new DuplicateEntityException("Emballage", "code", emballageDto.getCode());
        }
        
        // Update fields but preserve the ID
        ObjectMapperUtils.updateEntityFromDto(emballageDto, existingEmballage);
        existingEmballage.setId(id);
        
        existingEmballage = emballageRepository.save(existingEmballage);
        
        return ObjectMapperUtils.mapToDto(existingEmballage, EmballageDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public EmballageDto findById(Long id) {
        log.info("Finding emballage by ID: {}", id);
        
        return emballageRepository.findById(id)
                .map(emballage -> ObjectMapperUtils.mapToDto(emballage, EmballageDto.class))
                .orElseThrow(() -> new EntityNotFoundException("Emballage", "id", id));
    }

    @Override
    @Transactional(readOnly = true)
    public EmballageDto findByCode(String code) {
        log.info("Finding emballage by code: {}", code);
        
        return emballageRepository.findByCode(code)
                .map(emballage -> ObjectMapperUtils.mapToDto(emballage, EmballageDto.class))
                .orElseThrow(() -> new EntityNotFoundException("Emballage", "code", code));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmballageDto> findByLibelle(String libelle) {
        log.info("Finding emballages by libelle containing: {}", libelle);
        
        List<Emballage> emballages = emballageRepository.findByLibelleContainingIgnoreCase(libelle);
        return ObjectMapperUtils.mapAll(emballages, EmballageDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmballageDto> findByTypeEmballage(String typeEmballage) {
        log.info("Finding emballages by type: {}", typeEmballage);
        
        List<Emballage> emballages = emballageRepository.findByTypeEmballage(typeEmballage);
        return ObjectMapperUtils.mapAll(emballages, EmballageDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmballageDto> findAll() {
        log.info("Finding all emballages");
        
        List<Emballage> emballages = emballageRepository.findAll();
        return ObjectMapperUtils.mapAll(emballages, EmballageDto.class);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Deleting emballage with ID: {}", id);
        
        Emballage emballage = emballageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Emballage", "id", id));
        
        // TODO: Check if emballage is used by any products before deletion
        
        emballageRepository.delete(emballage);
    }
} 