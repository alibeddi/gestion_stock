package com.polytech.gestionstock.repository;

import com.polytech.gestionstock.model.entity.Devis;
import com.polytech.gestionstock.model.entity.LigneDevis;
import com.polytech.gestionstock.model.entity.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LigneDevisRepository extends JpaRepository<LigneDevis, Long> {
    List<LigneDevis> findByDevis(Devis devis);
    List<LigneDevis> findByProduit(Produit produit);
    
    @Query("SELECT ld FROM LigneDevis ld WHERE ld.devis.id = ?1")
    List<LigneDevis> findByDevisId(Long devisId);
    
    @Query("SELECT SUM(ld.totalTTC) FROM LigneDevis ld WHERE ld.devis.id = ?1")
    Double calculateTotalTTC(Long devisId);
    
    @Query("SELECT SUM(ld.poidsTotal) FROM LigneDevis ld WHERE ld.devis.id = ?1")
    Double calculateTotalPoids(Long devisId);
} 