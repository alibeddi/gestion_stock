package com.polytech.gestionstock.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.polytech.gestionstock.exception.DuplicateEntityException;
import com.polytech.gestionstock.exception.EntityNotFoundException;
import com.polytech.gestionstock.model.dto.SecteurActiviteDto;
import com.polytech.gestionstock.model.entity.SecteurActivite;
import com.polytech.gestionstock.repository.SecteurActiviteRepository;
import com.polytech.gestionstock.service.SecteurActiviteService;
import com.polytech.gestionstock.util.ObjectMapperUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SecteurActiviteServiceImpl implements SecteurActiviteService {

    private final SecteurActiviteRepository secteurActiviteRepository;

    @Override
    @Transactional
    public SecteurActiviteDto save(SecteurActiviteDto secteurActiviteDto) {
        log.info("Saving secteur d'activité with code: {}", secteurActiviteDto.getCode());
        
        // Check if secteur with same code already exists
        if (secteurActiviteRepository.existsByCode(secteurActiviteDto.getCode())) {
            throw new DuplicateEntityException("SecteurActivite", "code", secteurActiviteDto.getCode());
        }
        
        SecteurActivite secteurActivite = ObjectMapperUtils.mapToEntity(secteurActiviteDto, SecteurActivite.class);
        secteurActivite = secteurActiviteRepository.save(secteurActivite);
        
        return ObjectMapperUtils.mapToDto(secteurActivite, SecteurActiviteDto.class);
    }

    @Override
    @Transactional
    public SecteurActiviteDto update(Long id, SecteurActiviteDto secteurActiviteDto) {
        log.info("Updating secteur d'activité with ID: {}", id);
        
        SecteurActivite existingSecteur = secteurActiviteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("SecteurActivite", "id", id));
        
        // Check if code is changed and already exists for another secteur
        if (secteurActiviteDto.getCode() != null && 
            !secteurActiviteDto.getCode().equals(existingSecteur.getCode()) && 
            secteurActiviteRepository.existsByCode(secteurActiviteDto.getCode())) {
            throw new DuplicateEntityException("SecteurActivite", "code", secteurActiviteDto.getCode());
        }
        
        // Update fields but preserve ID
        ObjectMapperUtils.updateEntityFromDto(secteurActiviteDto, existingSecteur);
        existingSecteur.setId(id);
        
        existingSecteur = secteurActiviteRepository.save(existingSecteur);
        
        return ObjectMapperUtils.mapToDto(existingSecteur, SecteurActiviteDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public SecteurActiviteDto findById(Long id) {
        log.info("Finding secteur d'activité by ID: {}", id);
        
        SecteurActivite secteurActivite = secteurActiviteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("SecteurActivite", "id", id));
        
        return ObjectMapperUtils.mapToDto(secteurActivite, SecteurActiviteDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public SecteurActiviteDto findByCode(String code) {
        log.info("Finding secteur d'activité by code: {}", code);
        
        SecteurActivite secteurActivite = secteurActiviteRepository.findByCode(code)
                .orElseThrow(() -> new EntityNotFoundException("SecteurActivite", "code", code));
        
        return ObjectMapperUtils.mapToDto(secteurActivite, SecteurActiviteDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SecteurActiviteDto> findAll() {
        log.info("Finding all secteurs d'activité");
        
        List<SecteurActivite> secteursActivite = secteurActiviteRepository.findAll();
        
        return ObjectMapperUtils.mapAll(secteursActivite, SecteurActiviteDto.class);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Deleting secteur d'activité with ID: {}", id);
        
        if (!secteurActiviteRepository.existsById(id)) {
            throw new EntityNotFoundException("SecteurActivite", "id", id);
        }
        
        secteurActiviteRepository.deleteById(id);
    }
} 