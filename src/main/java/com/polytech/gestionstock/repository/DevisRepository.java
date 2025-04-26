package com.polytech.gestionstock.repository;

import com.polytech.gestionstock.model.entity.Client;
import com.polytech.gestionstock.model.entity.Devis;
import com.polytech.gestionstock.model.entity.Prospect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DevisRepository extends JpaRepository<Devis, Long> {
    Optional<Devis> findByNumeroDevis(String numeroDevis);
    List<Devis> findByClient(Client client);
    List<Devis> findByProspect(Prospect prospect);
    List<Devis> findBySujetContainingIgnoreCase(String sujet);
    List<Devis> findByEcheance(LocalDate echeance);
    List<Devis> findByEcheanceAfter(LocalDate date);
    List<Devis> findByEcheanceBefore(LocalDate date);
    
    @Query("SELECT d FROM Devis d ORDER BY d.dateCreation DESC")
    List<Devis> findAllOrderByDateCreationDesc();
    
    boolean existsByNumeroDevis(String numeroDevis);
} 