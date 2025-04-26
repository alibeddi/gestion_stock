package com.polytech.gestionstock.repository;

import com.polytech.gestionstock.model.entity.SourceProspection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SourceProspectionRepository extends JpaRepository<SourceProspection, Long> {
    Optional<SourceProspection> findByCode(String code);
    Optional<SourceProspection> findByLibelle(String libelle);
    boolean existsByCode(String code);
} 