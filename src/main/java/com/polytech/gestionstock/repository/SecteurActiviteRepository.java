package com.polytech.gestionstock.repository;

import com.polytech.gestionstock.model.entity.SecteurActivite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SecteurActiviteRepository extends JpaRepository<SecteurActivite, Long> {
    Optional<SecteurActivite> findByCode(String code);
    boolean existsByCode(String code);
    Optional<SecteurActivite> findByLibelle(String libelle);
} 