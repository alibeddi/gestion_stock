package com.polytech.gestionstock.service;

import com.polytech.gestionstock.model.dto.DevisDto;
import com.polytech.gestionstock.model.dto.LigneDevisDto;

import java.time.LocalDate;
import java.util.List;

public interface DevisService {
    DevisDto save(DevisDto devisDto);
    DevisDto update(Long id, DevisDto devisDto);
    DevisDto findById(Long id);
    DevisDto findByNumeroDevis(String numeroDevis);
    List<DevisDto> findByClientId(Long clientId);
    List<DevisDto> findByProspectId(Long prospectId);
    List<DevisDto> findBySujet(String sujet);
    List<DevisDto> findByEcheance(LocalDate echeance);
    List<DevisDto> findByEcheanceAfter(LocalDate date);
    List<DevisDto> findByEcheanceBefore(LocalDate date);
    List<DevisDto> findAllOrderByDateCreationDesc();
    List<DevisDto> findAll();
    
    // Specific operations
    DevisDto addLigneDevis(Long devisId, LigneDevisDto ligneDevisDto);
    DevisDto removeLigneDevis(Long devisId, Long ligneDevisId);
    DevisDto calculateTotals(Long devisId);
    void delete(Long id);
} 