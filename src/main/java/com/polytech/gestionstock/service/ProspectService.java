package com.polytech.gestionstock.service;

import com.polytech.gestionstock.model.dto.ProspectDto;
import com.polytech.gestionstock.model.entity.StatutProspect;

import java.util.List;

public interface ProspectService {
    ProspectDto save(ProspectDto prospectDto);
    ProspectDto update(Long id, ProspectDto prospectDto);
    ProspectDto findById(Long id);
    List<ProspectDto> findByNom(String nom);
    List<ProspectDto> findBySociete(String societe);
    List<ProspectDto> findByStatut(StatutProspect statut);
    List<ProspectDto> findBySecteurActiviteId(Long secteurActiviteId);
    List<ProspectDto> findBySourceProspectionId(Long sourceProspectionId);
    List<ProspectDto> findByEmail(String email);
    List<ProspectDto> findAll();
    List<ProspectDto> findByStatutOrderByDateCreationDesc(StatutProspect statut);
    void delete(Long id);
} 