package com.polytech.gestionstock.service;

import java.util.List;
import java.util.Set;

import com.polytech.gestionstock.model.dto.PermissionDto;
import com.polytech.gestionstock.model.dto.RoleDto;

public interface RoleService {
    RoleDto save(RoleDto roleDto);
    
    RoleDto update(Long id, RoleDto roleDto);
    
    RoleDto findById(Long id);
    
    RoleDto findByName(String name);
    
    List<RoleDto> findAll();
    
    void delete(Long id);
    
    // Permission-related methods
    RoleDto assignPermissionsToRole(Long roleId, Set<Long> permissionIds);
    
    RoleDto removePermissionsFromRole(Long roleId, Set<Long> permissionIds);
    
    Set<PermissionDto> getPermissionsByRoleId(Long roleId);
} 