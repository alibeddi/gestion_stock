package com.polytech.gestionstock.repository;

import com.polytech.gestionstock.model.entity.Gouvernorat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GouvernoratRepository extends JpaRepository<Gouvernorat, Long> {
    Optional<Gouvernorat> findByCode(String code);
    List<Gouvernorat> findByPays(String pays);
    boolean existsByCode(String code);
} 