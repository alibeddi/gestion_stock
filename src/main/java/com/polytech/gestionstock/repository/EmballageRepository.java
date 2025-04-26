package com.polytech.gestionstock.repository;

import com.polytech.gestionstock.model.entity.Emballage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmballageRepository extends JpaRepository<Emballage, Long> {
    Optional<Emballage> findByCode(String code);
    List<Emballage> findByLibelleContainingIgnoreCase(String libelle);
    List<Emballage> findByTypeEmballage(String typeEmballage);
    boolean existsByCode(String code);
} 