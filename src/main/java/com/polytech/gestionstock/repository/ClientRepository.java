package com.polytech.gestionstock.repository;

import com.polytech.gestionstock.model.entity.Client;
import com.polytech.gestionstock.model.entity.Gouvernorat;
import com.polytech.gestionstock.model.entity.SecteurActivite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByNumeroCompte(String numeroCompte);
    Optional<Client> findByMatriculeFiscal(String matriculeFiscal);
    List<Client> findByNomContainingIgnoreCase(String nom);
    List<Client> findBySecteurActivite(SecteurActivite secteurActivite);
    List<Client> findByGouvernorat(Gouvernorat gouvernorat);
    List<Client> findByExonere(Boolean exonere);
    
    @Query("SELECT c FROM Client c ORDER BY c.dateCreation DESC")
    List<Client> findAllOrderByDateCreationDesc();
    
    @Query("SELECT COUNT(c) FROM Client c")
    long countAllClients();
} 