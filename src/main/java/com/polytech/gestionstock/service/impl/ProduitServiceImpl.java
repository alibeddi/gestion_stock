package com.polytech.gestionstock.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.polytech.gestionstock.exception.DuplicateEntityException;
import com.polytech.gestionstock.exception.EntityNotFoundException;
import com.polytech.gestionstock.model.dto.ProduitDto;
import com.polytech.gestionstock.model.entity.Emballage;
import com.polytech.gestionstock.model.entity.Produit;
import com.polytech.gestionstock.repository.EmballageRepository;
import com.polytech.gestionstock.repository.ProduitRepository;
import com.polytech.gestionstock.service.ProduitService;
import com.polytech.gestionstock.util.ObjectMapperUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProduitServiceImpl implements ProduitService {

    private final ProduitRepository produitRepository;
    private final EmballageRepository emballageRepository;

    @Override
    @Transactional
    public ProduitDto save(ProduitDto produitDto) {
        log.info("Saving produit with code: {}", produitDto.getCode());
        
        // Check if produit with same code already exists
        if (produitRepository.existsByCode(produitDto.getCode())) {
            throw new DuplicateEntityException("Produit", "code", produitDto.getCode());
        }
        
        Produit produit = ObjectMapperUtils.mapToEntity(produitDto, Produit.class);
        
        // Set emballage reference if provided
        if (produitDto.getEmballage() != null && produitDto.getEmballage().getId() != null) {
            Emballage emballage = emballageRepository.findById(produitDto.getEmballage().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Emballage", "id", produitDto.getEmballage().getId()));
            produit.setEmballage(emballage);
        }
        
        produit = produitRepository.save(produit);
        
        return ObjectMapperUtils.mapToDto(produit, ProduitDto.class);
    }

    @Override
    @Transactional
    public ProduitDto update(Long id, ProduitDto produitDto) {
        log.info("Updating produit with ID: {}", id);
        
        Produit existingProduit = produitRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produit", "id", id));
        
        // Check if code is changed and already exists for another produit
        if (produitDto.getCode() != null && 
            !produitDto.getCode().equals(existingProduit.getCode()) && 
            produitRepository.existsByCode(produitDto.getCode())) {
            throw new DuplicateEntityException("Produit", "code", produitDto.getCode());
        }
        
        // Update fields but preserve ID
        ObjectMapperUtils.updateEntityFromDto(produitDto, existingProduit);
        existingProduit.setId(id);
        
        // Set emballage reference if provided
        if (produitDto.getEmballage() != null && produitDto.getEmballage().getId() != null) {
            Emballage emballage = emballageRepository.findById(produitDto.getEmballage().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Emballage", "id", produitDto.getEmballage().getId()));
            existingProduit.setEmballage(emballage);
        } else {
            existingProduit.setEmballage(null);
        }
        
        existingProduit = produitRepository.save(existingProduit);
        
        return ObjectMapperUtils.mapToDto(existingProduit, ProduitDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public ProduitDto findById(Long id) {
        log.info("Finding produit by ID: {}", id);
        
        Produit produit = produitRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produit", "id", id));
        
        return ObjectMapperUtils.mapToDto(produit, ProduitDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public ProduitDto findByCode(String code) {
        log.info("Finding produit by code: {}", code);
        
        Produit produit = produitRepository.findByCode(code)
                .orElseThrow(() -> new EntityNotFoundException("Produit", "code", code));
        
        return ObjectMapperUtils.mapToDto(produit, ProduitDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProduitDto> findByLibelle(String libelle) {
        log.info("Finding produits by libelle containing: {}", libelle);
        
        List<Produit> produits = produitRepository.findByLibelleContainingIgnoreCase(libelle);
        
        return ObjectMapperUtils.mapAll(produits, ProduitDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProduitDto> findByEmballageId(Long emballageId) {
        log.info("Finding produits by emballage ID: {}", emballageId);
        
        Emballage emballage = emballageRepository.findById(emballageId)
                .orElseThrow(() -> new EntityNotFoundException("Emballage", "id", emballageId));
        
        List<Produit> produits = produitRepository.findByEmballage(emballage);
        
        return ObjectMapperUtils.mapAll(produits, ProduitDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProduitDto> findByCategorie(String categorie) {
        log.info("Finding produits by categorie: {}", categorie);
        
        List<Produit> produits = produitRepository.findByCategorie(categorie);
        
        return ObjectMapperUtils.mapAll(produits, ProduitDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProduitDto> findByTypeProduit(String typeProduit) {
        log.info("Finding produits by type produit: {}", typeProduit);
        
        List<Produit> produits = produitRepository.findByTypeProduit(typeProduit);
        
        return ObjectMapperUtils.mapAll(produits, ProduitDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProduitDto> findByActif(Boolean actif) {
        log.info("Finding produits by actif: {}", actif);
        
        List<Produit> produits = produitRepository.findByActif(actif);
        
        return ObjectMapperUtils.mapAll(produits, ProduitDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProduitDto> findByIsPackage(Boolean isPackage) {
        log.info("Finding produits by isPackage: {}", isPackage);
        
        List<Produit> produits = produitRepository.findByIsPackage(isPackage);
        
        return ObjectMapperUtils.mapAll(produits, ProduitDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProduitDto> findByEcozit(Boolean ecozit) {
        log.info("Finding produits by ecozit: {}", ecozit);
        
        List<Produit> produits = produitRepository.findByEcozit(ecozit);
        
        return ObjectMapperUtils.mapAll(produits, ProduitDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProduitDto> findAllActiveProducts() {
        log.info("Finding all active products");
        
        List<Produit> produits = produitRepository.findAllActiveProducts();
        
        return ObjectMapperUtils.mapAll(produits, ProduitDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProduitDto> findAll() {
        log.info("Finding all produits");
        
        List<Produit> produits = produitRepository.findAll();
        
        return ObjectMapperUtils.mapAll(produits, ProduitDto.class);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Deleting produit with ID: {}", id);
        
        if (!produitRepository.existsById(id)) {
            throw new EntityNotFoundException("Produit", "id", id);
        }
        
        produitRepository.deleteById(id);
    }
} 