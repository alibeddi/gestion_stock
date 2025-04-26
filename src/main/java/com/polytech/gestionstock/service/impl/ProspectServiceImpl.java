package com.polytech.gestionstock.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.polytech.gestionstock.exception.EntityNotFoundException;
import com.polytech.gestionstock.model.dto.ProspectDto;
import com.polytech.gestionstock.model.entity.Prospect;
import com.polytech.gestionstock.model.entity.SecteurActivite;
import com.polytech.gestionstock.model.entity.SourceProspection;
import com.polytech.gestionstock.model.entity.StatutProspect;
import com.polytech.gestionstock.repository.ProspectRepository;
import com.polytech.gestionstock.repository.SecteurActiviteRepository;
import com.polytech.gestionstock.repository.SourceProspectionRepository;
import com.polytech.gestionstock.service.ProspectService;
import com.polytech.gestionstock.util.ObjectMapperUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProspectServiceImpl implements ProspectService {

    private final ProspectRepository prospectRepository;
    private final SecteurActiviteRepository secteurActiviteRepository;
    private final SourceProspectionRepository sourceProspectionRepository;

    @Override
    @Transactional
    public ProspectDto save(ProspectDto prospectDto) {
        log.info("Saving prospect with name: {}", prospectDto.getNom());
        
        Prospect prospect = ObjectMapperUtils.mapToEntity(prospectDto, Prospect.class);
        
        // Set statut if not provided
        if (prospect.getStatut() == null) {
            prospect.setStatut(StatutProspect.NOUVEAU);
        }
        
        // Set references
        if (prospectDto.getSecteurActivite() != null && prospectDto.getSecteurActivite().getId() != null) {
            SecteurActivite secteurActivite = secteurActiviteRepository.findById(prospectDto.getSecteurActivite().getId())
                    .orElseThrow(() -> new EntityNotFoundException("SecteurActivite", "id", prospectDto.getSecteurActivite().getId()));
            prospect.setSecteurActivite(secteurActivite);
        }
        
        if (prospectDto.getSourceProspection() != null && prospectDto.getSourceProspection().getId() != null) {
            SourceProspection sourceProspection = sourceProspectionRepository.findById(prospectDto.getSourceProspection().getId())
                    .orElseThrow(() -> new EntityNotFoundException("SourceProspection", "id", prospectDto.getSourceProspection().getId()));
            prospect.setSourceProspection(sourceProspection);
        }
        
        prospect = prospectRepository.save(prospect);
        return ObjectMapperUtils.mapToDto(prospect, ProspectDto.class);
    }

    @Override
    @Transactional
    public ProspectDto update(Long id, ProspectDto prospectDto) {
        log.info("Updating prospect with ID: {}", id);
        
        Prospect existingProspect = prospectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Prospect", "id", id));
        
        // Update fields but preserve ID
        ObjectMapperUtils.updateEntityFromDto(prospectDto, existingProspect);
        existingProspect.setId(id);
        
        // Set references
        if (prospectDto.getSecteurActivite() != null && prospectDto.getSecteurActivite().getId() != null) {
            SecteurActivite secteurActivite = secteurActiviteRepository.findById(prospectDto.getSecteurActivite().getId())
                    .orElseThrow(() -> new EntityNotFoundException("SecteurActivite", "id", prospectDto.getSecteurActivite().getId()));
            existingProspect.setSecteurActivite(secteurActivite);
        }
        
        if (prospectDto.getSourceProspection() != null && prospectDto.getSourceProspection().getId() != null) {
            SourceProspection sourceProspection = sourceProspectionRepository.findById(prospectDto.getSourceProspection().getId())
                    .orElseThrow(() -> new EntityNotFoundException("SourceProspection", "id", prospectDto.getSourceProspection().getId()));
            existingProspect.setSourceProspection(sourceProspection);
        }
        
        existingProspect = prospectRepository.save(existingProspect);
        return ObjectMapperUtils.mapToDto(existingProspect, ProspectDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public ProspectDto findById(Long id) {
        log.info("Finding prospect by ID: {}", id);
        
        return prospectRepository.findById(id)
                .map(prospect -> ObjectMapperUtils.mapToDto(prospect, ProspectDto.class))
                .orElseThrow(() -> new EntityNotFoundException("Prospect", "id", id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProspectDto> findByNom(String nom) {
        log.info("Finding prospects by nom containing: {}", nom);
        
        List<Prospect> prospects = prospectRepository.findByNomContainingIgnoreCase(nom);
        return ObjectMapperUtils.mapAll(prospects, ProspectDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProspectDto> findBySociete(String societe) {
        log.info("Finding prospects by société containing: {}", societe);
        
        List<Prospect> prospects = prospectRepository.findBySocieteContainingIgnoreCase(societe);
        return ObjectMapperUtils.mapAll(prospects, ProspectDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProspectDto> findByStatut(StatutProspect statut) {
        log.info("Finding prospects by statut: {}", statut);
        
        List<Prospect> prospects = prospectRepository.findByStatut(statut);
        return ObjectMapperUtils.mapAll(prospects, ProspectDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProspectDto> findBySecteurActiviteId(Long secteurActiviteId) {
        log.info("Finding prospects by secteur activité ID: {}", secteurActiviteId);
        
        SecteurActivite secteurActivite = secteurActiviteRepository.findById(secteurActiviteId)
                .orElseThrow(() -> new EntityNotFoundException("SecteurActivite", "id", secteurActiviteId));
        
        List<Prospect> prospects = prospectRepository.findBySecteurActivite(secteurActivite);
        return ObjectMapperUtils.mapAll(prospects, ProspectDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProspectDto> findBySourceProspectionId(Long sourceProspectionId) {
        log.info("Finding prospects by source prospection ID: {}", sourceProspectionId);
        
        SourceProspection sourceProspection = sourceProspectionRepository.findById(sourceProspectionId)
                .orElseThrow(() -> new EntityNotFoundException("SourceProspection", "id", sourceProspectionId));
        
        List<Prospect> prospects = prospectRepository.findBySourceProspection(sourceProspection);
        return ObjectMapperUtils.mapAll(prospects, ProspectDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProspectDto> findByEmail(String email) {
        log.info("Finding prospects by email containing: {}", email);
        
        List<Prospect> prospects = prospectRepository.findByEmailContainingIgnoreCase(email);
        return ObjectMapperUtils.mapAll(prospects, ProspectDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProspectDto> findAll() {
        log.info("Finding all prospects");
        
        List<Prospect> prospects = prospectRepository.findAll();
        return ObjectMapperUtils.mapAll(prospects, ProspectDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProspectDto> findByStatutOrderByDateCreationDesc(StatutProspect statut) {
        log.info("Finding prospects by statut: {} ordered by creation date desc", statut);
        
        List<Prospect> prospects = prospectRepository.findByStatutOrderByDateCreationDesc(statut);
        return ObjectMapperUtils.mapAll(prospects, ProspectDto.class);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Deleting prospect with ID: {}", id);
        
        Prospect prospect = prospectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Prospect", "id", id));
        
        prospectRepository.delete(prospect);
    }
} 