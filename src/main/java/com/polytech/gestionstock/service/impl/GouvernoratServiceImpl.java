package com.polytech.gestionstock.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.polytech.gestionstock.exception.DuplicateEntityException;
import com.polytech.gestionstock.exception.EntityNotFoundException;
import com.polytech.gestionstock.model.dto.GouvernoratDto;
import com.polytech.gestionstock.model.entity.Gouvernorat;
import com.polytech.gestionstock.repository.GouvernoratRepository;
import com.polytech.gestionstock.service.GouvernoratService;
import com.polytech.gestionstock.util.ObjectMapperUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class GouvernoratServiceImpl implements GouvernoratService {

    private final GouvernoratRepository gouvernoratRepository;

    @Override
    @Transactional
    public GouvernoratDto save(GouvernoratDto gouvernoratDto) {
        log.info("Saving gouvernorat with code: {}", gouvernoratDto.getCode());
        
        // Check if gouvernorat with same code already exists
        if (gouvernoratRepository.existsByCode(gouvernoratDto.getCode())) {
            throw new DuplicateEntityException("Gouvernorat", "code", gouvernoratDto.getCode());
        }
        
        Gouvernorat gouvernorat = ObjectMapperUtils.mapToEntity(gouvernoratDto, Gouvernorat.class);
        gouvernorat = gouvernoratRepository.save(gouvernorat);
        
        return ObjectMapperUtils.mapToDto(gouvernorat, GouvernoratDto.class);
    }

    @Override
    @Transactional
    public GouvernoratDto update(Long id, GouvernoratDto gouvernoratDto) {
        log.info("Updating gouvernorat with ID: {}", id);
        
        Gouvernorat existingGouvernorat = gouvernoratRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Gouvernorat", "id", id));
        
        // Check if code is changed and already exists for another gouvernorat
        if (gouvernoratDto.getCode() != null && 
            !gouvernoratDto.getCode().equals(existingGouvernorat.getCode()) && 
            gouvernoratRepository.existsByCode(gouvernoratDto.getCode())) {
            throw new DuplicateEntityException("Gouvernorat", "code", gouvernoratDto.getCode());
        }
        
        // Update fields but preserve ID
        ObjectMapperUtils.updateEntityFromDto(gouvernoratDto, existingGouvernorat);
        existingGouvernorat.setId(id);
        
        existingGouvernorat = gouvernoratRepository.save(existingGouvernorat);
        
        return ObjectMapperUtils.mapToDto(existingGouvernorat, GouvernoratDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public GouvernoratDto findById(Long id) {
        log.info("Finding gouvernorat by ID: {}", id);
        
        Gouvernorat gouvernorat = gouvernoratRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Gouvernorat", "id", id));
        
        return ObjectMapperUtils.mapToDto(gouvernorat, GouvernoratDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public GouvernoratDto findByCode(String code) {
        log.info("Finding gouvernorat by code: {}", code);
        
        Gouvernorat gouvernorat = gouvernoratRepository.findByCode(code)
                .orElseThrow(() -> new EntityNotFoundException("Gouvernorat", "code", code));
        
        return ObjectMapperUtils.mapToDto(gouvernorat, GouvernoratDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GouvernoratDto> findByPays(String pays) {
        log.info("Finding gouvernorats by pays: {}", pays);
        
        List<Gouvernorat> gouvernorats = gouvernoratRepository.findByPays(pays);
        
        return ObjectMapperUtils.mapAll(gouvernorats, GouvernoratDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GouvernoratDto> findAll() {
        log.info("Finding all gouvernorats");
        
        List<Gouvernorat> gouvernorats = gouvernoratRepository.findAll();
        
        return ObjectMapperUtils.mapAll(gouvernorats, GouvernoratDto.class);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Deleting gouvernorat with ID: {}", id);
        
        if (!gouvernoratRepository.existsById(id)) {
            throw new EntityNotFoundException("Gouvernorat", "id", id);
        }
        
        gouvernoratRepository.deleteById(id);
    }
} 