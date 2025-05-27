package com.polytech.gestionstock.service;

import java.util.List;

import com.polytech.gestionstock.model.dto.PermissionDto;

public interface PermissionService {
    PermissionDto save(PermissionDto permissionDto);
    
    PermissionDto update(Long id, PermissionDto permissionDto);
    
    PermissionDto findById(Long id);
    
    PermissionDto findByName(String name);
    
    List<PermissionDto> findByCategory(String category);
    
    List<PermissionDto> findByNameContaining(String name);
    
    List<PermissionDto> findAll();
    
    void delete(Long id);
    
    List<PermissionDto> initDefaultPermissions();
} 