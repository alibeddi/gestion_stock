package com.polytech.gestionstock.service;

import java.util.List;

import com.polytech.gestionstock.model.dto.ClientDto;
import com.polytech.gestionstock.model.dto.ProspectDto;

public interface ClientService {
    ClientDto save(ClientDto clientDto);
    ClientDto update(Long id, ClientDto clientDto);
    ClientDto findById(Long id);
    ClientDto findByNumeroCompte(String numeroCompte);
    ClientDto findByMatriculeFiscal(String matriculeFiscal);
    List<ClientDto> findByNom(String nom);
    List<ClientDto> findBySecteurActiviteId(Long secteurActiviteId);
    List<ClientDto> findByGouvernoratId(Long gouvernoratId);
    List<ClientDto> findByExonere(Boolean exonere);
    List<ClientDto> findAll();
    List<ClientDto> findAllOrderByDateCreationDesc();
    List<ClientDto> findWithFilters(String nom, String matriculeFiscal, String email, 
                                   Long secteurActiviteId, String sourceProspection, 
                                   String statut, int page, int size);
    long countAll();
    ClientDto convertProspectToClient(ProspectDto prospectDto);
    void delete(Long id);
} 