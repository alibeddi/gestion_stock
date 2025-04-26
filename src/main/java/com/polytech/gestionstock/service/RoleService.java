package com.polytech.gestionstock.service;

import com.polytech.gestionstock.model.dto.RoleDto;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    RoleDto save(RoleDto roleDto);
    RoleDto findById(Long id);
    RoleDto findByName(String name);
    List<RoleDto> findAll();
    void delete(Long id);
} 