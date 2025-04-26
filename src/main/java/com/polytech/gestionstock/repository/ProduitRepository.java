package com.polytech.gestionstock.repository;

import com.polytech.gestionstock.model.entity.Emballage;
import com.polytech.gestionstock.model.entity.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProduitRepository extends JpaRepository<Produit, Long> {
    Optional<Produit> findByCode(String code);
    List<Produit> findByLibelleContainingIgnoreCase(String libelle);
    List<Produit> findByEmballage(Emballage emballage);
    List<Produit> findByCategorie(String categorie);
    List<Produit> findByTypeProduit(String typeProduit);
    List<Produit> findByActif(Boolean actif);
    List<Produit> findByIsPackage(Boolean isPackage);
    List<Produit> findByEcozit(Boolean ecozit);
    
    @Query("SELECT p FROM Produit p WHERE p.actif = true ORDER BY p.libelle ASC")
    List<Produit> findAllActiveProducts();
    
    boolean existsByCode(String code);
} 