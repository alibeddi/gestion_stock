package com.polytech.gestionstock.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.polytech.gestionstock.exception.EntityNotFoundException;
import com.polytech.gestionstock.exception.InvalidOperationException;
import com.polytech.gestionstock.model.dto.DevisDto;
import com.polytech.gestionstock.model.dto.LigneDevisDto;
import com.polytech.gestionstock.model.entity.Client;
import com.polytech.gestionstock.model.entity.Devis;
import com.polytech.gestionstock.model.entity.LigneDevis;
import com.polytech.gestionstock.model.entity.Produit;
import com.polytech.gestionstock.model.entity.Prospect;
import com.polytech.gestionstock.repository.ClientRepository;
import com.polytech.gestionstock.repository.DevisRepository;
import com.polytech.gestionstock.repository.LigneDevisRepository;
import com.polytech.gestionstock.repository.ProduitRepository;
import com.polytech.gestionstock.repository.ProspectRepository;
import com.polytech.gestionstock.service.DevisService;
import com.polytech.gestionstock.util.IdGenerator;
import com.polytech.gestionstock.util.ObjectMapperUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DevisServiceImpl implements DevisService {

    private final DevisRepository devisRepository;
    private final LigneDevisRepository ligneDevisRepository;
    private final ClientRepository clientRepository;
    private final ProspectRepository prospectRepository;
    private final ProduitRepository produitRepository;

    @Override
    @Transactional
    public DevisDto save(DevisDto devisDto) {
        log.info("Saving devis with subject: {}", devisDto.getSujet());
        
        // Generate a unique devis number if not provided
        if (devisDto.getNumeroDevis() == null || devisDto.getNumeroDevis().isEmpty()) {
            devisDto.setNumeroDevis(IdGenerator.generateDevisNumber());
        } else if (devisRepository.existsByNumeroDevis(devisDto.getNumeroDevis())) {
            throw new InvalidOperationException("Devis number already exists: " + devisDto.getNumeroDevis());
        }
        
        validateDevisReferences(devisDto);
        
        Devis devis = ObjectMapperUtils.mapToEntity(devisDto, Devis.class);
        devis = devisRepository.save(devis);
        
        // Save lignes devis
        if (devisDto.getLignesDevis() != null && !devisDto.getLignesDevis().isEmpty()) {
            for (LigneDevisDto ligneDto : devisDto.getLignesDevis()) {
                addLigneDevisToEntity(devis, ligneDto);
            }
        }
        
        // Calculate totals
        calculateTotalsEntity(devis);
        
        // Save again with updated totals
        devis = devisRepository.save(devis);
        
        return ObjectMapperUtils.mapToDto(devis, DevisDto.class);
    }

    @Override
    @Transactional
    public DevisDto update(Long id, DevisDto devisDto) {
        log.info("Updating devis with ID: {}", id);
        
        Devis existingDevis = devisRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Devis", "id", id));
        
        // Check numero devis if changed
        if (devisDto.getNumeroDevis() != null && !devisDto.getNumeroDevis().equals(existingDevis.getNumeroDevis()) &&
                devisRepository.existsByNumeroDevis(devisDto.getNumeroDevis())) {
            throw new InvalidOperationException("Devis number already exists: " + devisDto.getNumeroDevis());
        }
        
        validateDevisReferences(devisDto);
        
        // Update fields but preserve the ID
        ObjectMapperUtils.updateEntityFromDto(devisDto, existingDevis);
        existingDevis.setId(id);
        
        // Save the updated devis without its lines
        existingDevis = devisRepository.save(existingDevis);
        
        // If lignes devis are provided, replace them entirely
        if (devisDto.getLignesDevis() != null) {
            // Clear existing lines
            ligneDevisRepository.findByDevis(existingDevis).forEach(ligneDevisRepository::delete);
            existingDevis.getLignesDevis().clear();
            
            // Add new lines
            for (LigneDevisDto ligneDto : devisDto.getLignesDevis()) {
                addLigneDevisToEntity(existingDevis, ligneDto);
            }
        }
        
        // Calculate totals
        calculateTotalsEntity(existingDevis);
        
        // Save again with updated totals
        existingDevis = devisRepository.save(existingDevis);
        
        return ObjectMapperUtils.mapToDto(existingDevis, DevisDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public DevisDto findById(Long id) {
        log.info("Finding devis by ID: {}", id);
        
        return devisRepository.findById(id)
                .map(devis -> ObjectMapperUtils.mapToDto(devis, DevisDto.class))
                .orElseThrow(() -> new EntityNotFoundException("Devis", "id", id));
    }

    @Override
    @Transactional(readOnly = true)
    public DevisDto findByNumeroDevis(String numeroDevis) {
        log.info("Finding devis by numero: {}", numeroDevis);
        
        return devisRepository.findByNumeroDevis(numeroDevis)
                .map(devis -> ObjectMapperUtils.mapToDto(devis, DevisDto.class))
                .orElseThrow(() -> new EntityNotFoundException("Devis", "numeroDevis", numeroDevis));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DevisDto> findByClientId(Long clientId) {
        log.info("Finding devis by client ID: {}", clientId);
        
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new EntityNotFoundException("Client", "id", clientId));
        
        List<Devis> devis = devisRepository.findByClient(client);
        return ObjectMapperUtils.mapAll(devis, DevisDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DevisDto> findByProspectId(Long prospectId) {
        log.info("Finding devis by prospect ID: {}", prospectId);
        
        Prospect prospect = prospectRepository.findById(prospectId)
                .orElseThrow(() -> new EntityNotFoundException("Prospect", "id", prospectId));
        
        List<Devis> devis = devisRepository.findByProspect(prospect);
        return ObjectMapperUtils.mapAll(devis, DevisDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DevisDto> findBySujet(String sujet) {
        log.info("Finding devis by sujet containing: {}", sujet);
        
        List<Devis> devis = devisRepository.findBySujetContainingIgnoreCase(sujet);
        return ObjectMapperUtils.mapAll(devis, DevisDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DevisDto> findByEcheance(LocalDate echeance) {
        log.info("Finding devis by echeance: {}", echeance);
        
        List<Devis> devis = devisRepository.findByEcheance(echeance);
        return ObjectMapperUtils.mapAll(devis, DevisDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DevisDto> findByEcheanceAfter(LocalDate date) {
        log.info("Finding devis by echeance after: {}", date);
        
        List<Devis> devis = devisRepository.findByEcheanceAfter(date);
        return ObjectMapperUtils.mapAll(devis, DevisDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DevisDto> findByEcheanceBefore(LocalDate date) {
        log.info("Finding devis by echeance before: {}", date);
        
        List<Devis> devis = devisRepository.findByEcheanceBefore(date);
        return ObjectMapperUtils.mapAll(devis, DevisDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DevisDto> findAllOrderByDateCreationDesc() {
        log.info("Finding all devis ordered by creation date descending");
        
        List<Devis> devis = devisRepository.findAllOrderByDateCreationDesc();
        return ObjectMapperUtils.mapAll(devis, DevisDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DevisDto> findAll() {
        log.info("Finding all devis");
        
        List<Devis> devis = devisRepository.findAll();
        return ObjectMapperUtils.mapAll(devis, DevisDto.class);
    }

    @Override
    @Transactional
    public DevisDto addLigneDevis(Long devisId, LigneDevisDto ligneDevisDto) {
        log.info("Adding ligne devis to devis with ID: {}", devisId);
        
        Devis devis = devisRepository.findById(devisId)
                .orElseThrow(() -> new EntityNotFoundException("Devis", "id", devisId));
        
        addLigneDevisToEntity(devis, ligneDevisDto);
        
        // Calculate totals
        calculateTotalsEntity(devis);
        
        // Save devis with updated totals
        devis = devisRepository.save(devis);
        
        return ObjectMapperUtils.mapToDto(devis, DevisDto.class);
    }

    @Override
    @Transactional
    public DevisDto removeLigneDevis(Long devisId, Long ligneDevisId) {
        log.info("Removing ligne devis with ID: {} from devis with ID: {}", ligneDevisId, devisId);
        
        Devis devis = devisRepository.findById(devisId)
                .orElseThrow(() -> new EntityNotFoundException("Devis", "id", devisId));
        
        LigneDevis ligneDevis = ligneDevisRepository.findById(ligneDevisId)
                .orElseThrow(() -> new EntityNotFoundException("LigneDevis", "id", ligneDevisId));
        
        // Verify that the ligne devis belongs to the devis
        if (!ligneDevis.getDevis().getId().equals(devisId)) {
            throw new InvalidOperationException("Ligne devis " + ligneDevisId + " does not belong to devis " + devisId);
        }
        
        // Remove the ligne devis
        devis.getLignesDevis().remove(ligneDevis);
        ligneDevisRepository.delete(ligneDevis);
        
        // Calculate totals
        calculateTotalsEntity(devis);
        
        // Save devis with updated totals
        devis = devisRepository.save(devis);
        
        return ObjectMapperUtils.mapToDto(devis, DevisDto.class);
    }

    @Override
    @Transactional
    public DevisDto calculateTotals(Long devisId) {
        log.info("Calculating totals for devis with ID: {}", devisId);
        
        Devis devis = devisRepository.findById(devisId)
                .orElseThrow(() -> new EntityNotFoundException("Devis", "id", devisId));
        
        calculateTotalsEntity(devis);
        
        devis = devisRepository.save(devis);
        
        return ObjectMapperUtils.mapToDto(devis, DevisDto.class);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Deleting devis with ID: {}", id);
        
        Devis devis = devisRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Devis", "id", id));
        
        // Delete associated lignes devis first
        ligneDevisRepository.findByDevis(devis).forEach(ligneDevisRepository::delete);
        
        // Delete the devis
        devisRepository.delete(devis);
    }
    
    // Helper methods
    
    private void validateDevisReferences(DevisDto devisDto) {
        // Client and prospect validation (one must be set, not both)
        if ((devisDto.getClient() == null || devisDto.getClient().getId() == null) &&
                (devisDto.getProspect() == null || devisDto.getProspect().getId() == null)) {
            throw new InvalidOperationException("Either client or prospect must be specified", "Missing required reference");
        }
        
        if (devisDto.getClient() != null && devisDto.getClient().getId() != null &&
                devisDto.getProspect() != null && devisDto.getProspect().getId() != null) {
            throw new InvalidOperationException("Cannot specify both client and prospect", "Conflicting references");
        }
        
        // Validate client if specified
        if (devisDto.getClient() != null && devisDto.getClient().getId() != null) {
            Client client = clientRepository.findById(devisDto.getClient().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Client", "id", devisDto.getClient().getId()));
            devisDto.setClient(ObjectMapperUtils.mapToDto(client, devisDto.getClient().getClass()));
        }
        
        // Validate prospect if specified
        if (devisDto.getProspect() != null && devisDto.getProspect().getId() != null) {
            Prospect prospect = prospectRepository.findById(devisDto.getProspect().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Prospect", "id", devisDto.getProspect().getId()));
            devisDto.setProspect(ObjectMapperUtils.mapToDto(prospect, devisDto.getProspect().getClass()));
        }
    }
    
    private void addLigneDevisToEntity(Devis devis, LigneDevisDto ligneDevisDto) {
        // Validate produit
        Produit produit = produitRepository.findById(ligneDevisDto.getProduit().getId())
                .orElseThrow(() -> new EntityNotFoundException("Produit", "id", ligneDevisDto.getProduit().getId()));
        
        LigneDevis ligneDevis = ObjectMapperUtils.mapToEntity(ligneDevisDto, LigneDevis.class);
        ligneDevis.setDevis(devis);
        ligneDevis.setProduit(produit);
        
        // Calculate line values
        BigDecimal prixUnitaireHT = ligneDevisDto.getPrixUnitaireHT();
        int quantite = ligneDevisDto.getQuantite();
        BigDecimal tauxTVA = ligneDevisDto.getTauxTVA();
        
        // Calculate TVA amount
        BigDecimal montantTVA = prixUnitaireHT.multiply(tauxTVA);
        ligneDevis.setMontantTVA(montantTVA);
        
        // Set ecozit if product has ecozit flag
        if (produit.getEcozit()) {
            BigDecimal ecozit = new BigDecimal("0.05"); // Example ecozit rate (5%)
            ligneDevis.setEcozit(prixUnitaireHT.multiply(ecozit));
        } else {
            ligneDevis.setEcozit(BigDecimal.ZERO);
        }
        
        // Calculate TTC price (HT + TVA + ecozit)
        BigDecimal prixTTC = prixUnitaireHT.add(montantTVA).add(ligneDevis.getEcozit());
        ligneDevis.setPrixTTC(prixTTC);
        
        // Calculate total TTC (prixTTC * quantite)
        BigDecimal totalTTC = prixTTC.multiply(new BigDecimal(quantite));
        ligneDevis.setTotalTTC(totalTTC);
        
        // Set total weight if product has weight
        if (produit.getPoidsKg() != null) {
            ligneDevis.setPoidsTotal(produit.getPoidsKg().multiply(new BigDecimal(quantite)));
        }
        
        // Save the ligne devis
        ligneDevisRepository.save(ligneDevis);
        
        // Add to devis
        devis.getLignesDevis().add(ligneDevis);
    }
    
    private void calculateTotalsEntity(Devis devis) {
        // Calculate total TTC
        Double totalTTC = ligneDevisRepository.calculateTotalTTC(devis.getId());
        if (totalTTC != null) {
            devis.setTotalTTC(new BigDecimal(totalTTC));
        } else {
            devis.setTotalTTC(BigDecimal.ZERO);
        }
        
        // Calculate total weight
        Double totalPoids = ligneDevisRepository.calculateTotalPoids(devis.getId());
        if (totalPoids != null) {
            devis.setTotalPoidsKg(new BigDecimal(totalPoids));
        } else {
            devis.setTotalPoidsKg(BigDecimal.ZERO);
        }
    }
} 