package com.polytech.gestionstock.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.polytech.gestionstock.exception.DuplicateEntityException;
import com.polytech.gestionstock.exception.EntityNotFoundException;
import com.polytech.gestionstock.model.dto.SourceProspectionDto;
import com.polytech.gestionstock.model.entity.SourceProspection;
import com.polytech.gestionstock.repository.SourceProspectionRepository;
import com.polytech.gestionstock.service.SourceProspectionService;
import com.polytech.gestionstock.util.ObjectMapperUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SourceProspectionServiceImpl implements SourceProspectionService {

    private final SourceProspectionRepository sourceProspectionRepository;

    @Override
    @Transactional
    public SourceProspectionDto save(SourceProspectionDto sourceProspectionDto) {
        log.info("Saving source de prospection with code: {}", sourceProspectionDto.getCode());
        
        // Check if source with same code already exists
        if (sourceProspectionRepository.existsByCode(sourceProspectionDto.getCode())) {
            throw new DuplicateEntityException("SourceProspection", "code", sourceProspectionDto.getCode());
        }
        
        SourceProspection sourceProspection = ObjectMapperUtils.mapToEntity(sourceProspectionDto, SourceProspection.class);
        sourceProspection = sourceProspectionRepository.save(sourceProspection);
        
        return ObjectMapperUtils.mapToDto(sourceProspection, SourceProspectionDto.class);
    }

    @Override
    @Transactional
    public SourceProspectionDto update(Long id, SourceProspectionDto sourceProspectionDto) {
        log.info("Updating source de prospection with ID: {}", id);
        
        SourceProspection existingSource = sourceProspectionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("SourceProspection", "id", id));
        
        // Check if code is changed and already exists for another source
        if (sourceProspectionDto.getCode() != null && 
            !sourceProspectionDto.getCode().equals(existingSource.getCode()) && 
            sourceProspectionRepository.existsByCode(sourceProspectionDto.getCode())) {
            throw new DuplicateEntityException("SourceProspection", "code", sourceProspectionDto.getCode());
        }
        
        // Update fields but preserve ID
        ObjectMapperUtils.updateEntityFromDto(sourceProspectionDto, existingSource);
        existingSource.setId(id);
        
        existingSource = sourceProspectionRepository.save(existingSource);
        
        return ObjectMapperUtils.mapToDto(existingSource, SourceProspectionDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public SourceProspectionDto findById(Long id) {
        log.info("Finding source de prospection by ID: {}", id);
        
        SourceProspection sourceProspection = sourceProspectionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("SourceProspection", "id", id));
        
        return ObjectMapperUtils.mapToDto(sourceProspection, SourceProspectionDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public SourceProspectionDto findByCode(String code) {
        log.info("Finding source de prospection by code: {}", code);
        
        SourceProspection sourceProspection = sourceProspectionRepository.findByCode(code)
                .orElseThrow(() -> new EntityNotFoundException("SourceProspection", "code", code));
        
        return ObjectMapperUtils.mapToDto(sourceProspection, SourceProspectionDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public SourceProspectionDto findByLibelle(String libelle) {
        log.info("Finding source de prospection by libelle: {}", libelle);
        
        SourceProspection sourceProspection = sourceProspectionRepository.findByLibelle(libelle)
                .orElseThrow(() -> new EntityNotFoundException("SourceProspection", "libelle", libelle));
        
        return ObjectMapperUtils.mapToDto(sourceProspection, SourceProspectionDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SourceProspectionDto> findAll() {
        log.info("Finding all sources de prospection");
        
        List<SourceProspection> sourcesProspection = sourceProspectionRepository.findAll();
        
        return ObjectMapperUtils.mapAll(sourcesProspection, SourceProspectionDto.class);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Deleting source de prospection with ID: {}", id);
        
        if (!sourceProspectionRepository.existsById(id)) {
            throw new EntityNotFoundException("SourceProspection", "id", id);
        }
        
        sourceProspectionRepository.deleteById(id);
    }
} 