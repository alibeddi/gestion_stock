package com.polytech.gestionstock.repository;

import com.polytech.gestionstock.model.entity.Prospect;
import com.polytech.gestionstock.model.entity.SecteurActivite;
import com.polytech.gestionstock.model.entity.SourceProspection;
import com.polytech.gestionstock.model.entity.StatutProspect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProspectRepository extends JpaRepository<Prospect, Long> {
    List<Prospect> findByNomContainingIgnoreCase(String nom);
    List<Prospect> findBySocieteContainingIgnoreCase(String societe);
    List<Prospect> findByStatut(StatutProspect statut);
    List<Prospect> findBySecteurActivite(SecteurActivite secteurActivite);
    List<Prospect> findBySourceProspection(SourceProspection sourceProspection);
    List<Prospect> findByEmailContainingIgnoreCase(String email);
    
    @Query("SELECT p FROM Prospect p WHERE p.statut = ?1 ORDER BY p.dateCreation DESC")
    List<Prospect> findByStatutOrderByDateCreationDesc(StatutProspect statut);
} 