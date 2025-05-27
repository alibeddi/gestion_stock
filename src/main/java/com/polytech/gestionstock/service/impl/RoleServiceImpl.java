package com.polytech.gestionstock.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.polytech.gestionstock.exception.DuplicateEntityException;
import com.polytech.gestionstock.exception.EntityNotFoundException;
import com.polytech.gestionstock.model.dto.PermissionDto;
import com.polytech.gestionstock.model.dto.RoleDto;
import com.polytech.gestionstock.model.entity.Permission;
import com.polytech.gestionstock.model.entity.Role;
import com.polytech.gestionstock.repository.PermissionRepository;
import com.polytech.gestionstock.repository.RoleRepository;
import com.polytech.gestionstock.service.RoleService;
import com.polytech.gestionstock.util.ObjectMapperUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @Override
    @Transactional
    public RoleDto save(RoleDto roleDto) {
        log.info("Saving role with name: {}", roleDto.getName());
        
        if (roleRepository.existsByName(roleDto.getName())) {
            throw new DuplicateEntityException("Role", "name", roleDto.getName());
        }
        
        Role role = ObjectMapperUtils.mapToEntity(roleDto, Role.class);
        
        // Handle permissions if provided
        if (roleDto.getPermissions() != null && !roleDto.getPermissions().isEmpty()) {
            Set<Permission> permissions = new HashSet<>();
            for (PermissionDto permissionDto : roleDto.getPermissions()) {
                if (permissionDto.getId() != null) {
                    Permission permission = permissionRepository.findById(permissionDto.getId())
                            .orElseThrow(() -> new EntityNotFoundException("Permission", "id", permissionDto.getId()));
                    permissions.add(permission);
                }
            }
            role.setPermissions(permissions);
        }
        
        role = roleRepository.save(role);
        
        return ObjectMapperUtils.mapToDto(role, RoleDto.class);
    }
    
    @Override
    @Transactional
    public RoleDto update(Long id, RoleDto roleDto) {
        log.info("Updating role with ID: {}", id);
        
        Role existingRole = roleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Role", "id", id));
        
        // Check if name is being changed and if it's already taken
        if (!existingRole.getName().equals(roleDto.getName()) && 
                roleRepository.existsByName(roleDto.getName())) {
            throw new DuplicateEntityException("Role", "name", roleDto.getName());
        }
        
        // Update basic fields
        existingRole.setName(roleDto.getName());
        existingRole.setLibelle(roleDto.getLibelle());
        
        // Handle permissions if provided
        if (roleDto.getPermissions() != null && !roleDto.getPermissions().isEmpty()) {
            Set<Permission> permissions = new HashSet<>();
            for (PermissionDto permissionDto : roleDto.getPermissions()) {
                if (permissionDto.getId() != null) {
                    Permission permission = permissionRepository.findById(permissionDto.getId())
                            .orElseThrow(() -> new EntityNotFoundException("Permission", "id", permissionDto.getId()));
                    permissions.add(permission);
                }
            }
            existingRole.setPermissions(permissions);
        }
        
        existingRole = roleRepository.save(existingRole);
        
        return ObjectMapperUtils.mapToDto(existingRole, RoleDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public RoleDto findById(Long id) {
        log.info("Finding role by ID: {}", id);
        
        return roleRepository.findById(id)
                .map(role -> ObjectMapperUtils.mapToDto(role, RoleDto.class))
                .orElseThrow(() -> new EntityNotFoundException("Role", "id", id));
    }

    @Override
    @Transactional(readOnly = true)
    public RoleDto findByName(String name) {
        log.info("Finding role by name: {}", name);
        
        return roleRepository.findByName(name)
                .map(role -> ObjectMapperUtils.mapToDto(role, RoleDto.class))
                .orElseThrow(() -> new EntityNotFoundException("Role", "name", name));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleDto> findAll() {
        log.info("Finding all roles");
        
        List<Role> roles = roleRepository.findAll();
        return ObjectMapperUtils.mapAll(roles, RoleDto.class);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Deleting role with ID: {}", id);
        
        if (!roleRepository.existsById(id)) {
            throw new EntityNotFoundException("Role", "id", id);
        }
        
        roleRepository.deleteById(id);
    }
    
    @Override
    @Transactional
    public RoleDto assignPermissionsToRole(Long roleId, Set<Long> permissionIds) {
        log.info("Assigning permissions {} to role with ID: {}", permissionIds, roleId);
        
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new EntityNotFoundException("Role", "id", roleId));
        
        Set<Permission> permissionsToAdd = new HashSet<>();
        for (Long permissionId : permissionIds) {
            Permission permission = permissionRepository.findById(permissionId)
                    .orElseThrow(() -> new EntityNotFoundException("Permission", "id", permissionId));
            permissionsToAdd.add(permission);
        }
        
        // Add new permissions to the existing set
        role.getPermissions().addAll(permissionsToAdd);
        
        role = roleRepository.save(role);
        
        return ObjectMapperUtils.mapToDto(role, RoleDto.class);
    }
    
    @Override
    @Transactional
    public RoleDto removePermissionsFromRole(Long roleId, Set<Long> permissionIds) {
        log.info("Removing permissions {} from role with ID: {}", permissionIds, roleId);
        
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new EntityNotFoundException("Role", "id", roleId));
        
        // Remove specified permissions
        role.setPermissions(role.getPermissions().stream()
                .filter(permission -> !permissionIds.contains(permission.getId()))
                .collect(Collectors.toSet()));
        
        role = roleRepository.save(role);
        
        return ObjectMapperUtils.mapToDto(role, RoleDto.class);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Set<PermissionDto> getPermissionsByRoleId(Long roleId) {
        log.info("Getting permissions for role with ID: {}", roleId);
        
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new EntityNotFoundException("Role", "id", roleId));
        
        return role.getPermissions().stream()
                .map(permission -> ObjectMapperUtils.mapToDto(permission, PermissionDto.class))
                .collect(Collectors.toSet());
    }
} 