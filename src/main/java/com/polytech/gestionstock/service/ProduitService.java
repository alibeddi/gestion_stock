package com.polytech.gestionstock.service;

import com.polytech.gestionstock.model.dto.ProduitDto;

import java.util.List;

public interface ProduitService {
    ProduitDto save(ProduitDto produitDto);
    ProduitDto update(Long id, ProduitDto produitDto);
    ProduitDto findById(Long id);
    ProduitDto findByCode(String code);
    List<ProduitDto> findByLibelle(String libelle);
    List<ProduitDto> findByEmballageId(Long emballageId);
    List<ProduitDto> findByCategorie(String categorie);
    List<ProduitDto> findByTypeProduit(String typeProduit);
    List<ProduitDto> findByActif(Boolean actif);
    List<ProduitDto> findByIsPackage(Boolean isPackage);
    List<ProduitDto> findByEcozit(Boolean ecozit);
    List<ProduitDto> findAllActiveProducts();
    List<ProduitDto> findAll();
    void delete(Long id);
} 