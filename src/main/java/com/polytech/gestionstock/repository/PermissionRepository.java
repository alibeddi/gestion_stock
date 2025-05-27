package com.polytech.gestionstock.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.polytech.gestionstock.model.entity.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Optional<Permission> findByName(String name);
    
    boolean existsByName(String name);
    
    List<Permission> findByCategory(String category);
    
    List<Permission> findByNameContainingIgnoreCase(String name);
} 