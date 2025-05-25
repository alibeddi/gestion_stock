package com.polytech.gestionstock.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.polytech.gestionstock.model.entity.PaymentMode;
import com.polytech.gestionstock.model.entity.User;

@Repository
public interface PaymentModeRepository extends JpaRepository<PaymentMode, Long> {
    boolean existsByName(String name);
    boolean existsByCode(String code);
    Optional<PaymentMode> findByCode(String code);
    List<PaymentMode> findByCreatedBy(User createdBy);
    List<PaymentMode> findByActive(boolean active);
} 