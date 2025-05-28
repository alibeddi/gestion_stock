package com.polytech.gestionstock.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.polytech.gestionstock.exception.DuplicateEntityException;
import com.polytech.gestionstock.exception.EntityNotFoundException;
import com.polytech.gestionstock.model.dto.PermissionDto;
import com.polytech.gestionstock.model.entity.Permission;
import com.polytech.gestionstock.repository.PermissionRepository;
import com.polytech.gestionstock.service.PermissionService;
import com.polytech.gestionstock.util.ObjectMapperUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;

    @Override
    @Transactional
    public PermissionDto save(PermissionDto permissionDto) {
        log.info("Saving permission with name: {}", permissionDto.getName());
        
        if (permissionRepository.existsByName(permissionDto.getName())) {
            throw new DuplicateEntityException("Permission", "name", permissionDto.getName());
        }
        
        Permission permission = ObjectMapperUtils.mapToEntity(permissionDto, Permission.class);
        permission = permissionRepository.save(permission);
        
        return ObjectMapperUtils.mapToDto(permission, PermissionDto.class);
    }

    @Override
    @Transactional
    public PermissionDto update(Long id, PermissionDto permissionDto) {
        log.info("Updating permission with ID: {}", id);
        
        Permission existingPermission = permissionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Permission", "id", id));
        
        // Check if name is being changed and if it's already taken
        if (!existingPermission.getName().equals(permissionDto.getName()) && 
                permissionRepository.existsByName(permissionDto.getName())) {
            throw new DuplicateEntityException("Permission", "name", permissionDto.getName());
        }
        
        // Update the permission details but preserve the original ID
        ObjectMapperUtils.updateEntityFromDto(permissionDto, existingPermission);
        existingPermission.setId(id);
        
        existingPermission = permissionRepository.save(existingPermission);
        return ObjectMapperUtils.mapToDto(existingPermission, PermissionDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public PermissionDto findById(Long id) {
        log.info("Finding permission by ID: {}", id);
        
        return permissionRepository.findById(id)
                .map(permission -> ObjectMapperUtils.mapToDto(permission, PermissionDto.class))
                .orElseThrow(() -> new EntityNotFoundException("Permission", "id", id));
    }

    @Override
    @Transactional(readOnly = true)
    public PermissionDto findByName(String name) {
        log.info("Finding permission by name: {}", name);
        
        return permissionRepository.findByName(name)
                .map(permission -> ObjectMapperUtils.mapToDto(permission, PermissionDto.class))
                .orElseThrow(() -> new EntityNotFoundException("Permission", "name", name));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PermissionDto> findByCategory(String category) {
        log.info("Finding permissions by category: {}", category);
        
        List<Permission> permissions = permissionRepository.findByCategory(category);
        return ObjectMapperUtils.mapAll(permissions, PermissionDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PermissionDto> findByNameContaining(String name) {
        log.info("Finding permissions by name containing: {}", name);
        
        List<Permission> permissions = permissionRepository.findByNameContainingIgnoreCase(name);
        return ObjectMapperUtils.mapAll(permissions, PermissionDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PermissionDto> findAll() {
        log.info("Finding all permissions");
        
        List<Permission> permissions = permissionRepository.findAll();
        return ObjectMapperUtils.mapAll(permissions, PermissionDto.class);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Deleting permission with ID: {}", id);
        
        if (!permissionRepository.existsById(id)) {
            throw new EntityNotFoundException("Permission", "id", id);
        }
        
        permissionRepository.deleteById(id);
    }

    @Override
    @Transactional
    public List<PermissionDto> initDefaultPermissions() {
        log.info("Initializing default permissions");
        
        List<PermissionDto> defaultPermissions = new ArrayList<>();
        
        // Users permissions
        createDefaultPermissionIfNotExists("users:read", "View users", "Users", defaultPermissions);
        createDefaultPermissionIfNotExists("users:create", "Create users", "Users", defaultPermissions);
        createDefaultPermissionIfNotExists("users:update", "Update users", "Users", defaultPermissions);
        createDefaultPermissionIfNotExists("users:delete", "Delete users", "Users", defaultPermissions);
        
        // Clients permissions
        createDefaultPermissionIfNotExists("clients:read", "View clients", "Clients", defaultPermissions);
        createDefaultPermissionIfNotExists("clients:create", "Create clients", "Clients", defaultPermissions);
        createDefaultPermissionIfNotExists("clients:update", "Update clients", "Clients", defaultPermissions);
        createDefaultPermissionIfNotExists("clients:delete", "Delete clients", "Clients", defaultPermissions);
        
        // Prospects permissions
        createDefaultPermissionIfNotExists("prospects:read", "View prospects", "Prospects", defaultPermissions);
        createDefaultPermissionIfNotExists("prospects:create", "Create prospects", "Prospects", defaultPermissions);
        createDefaultPermissionIfNotExists("prospects:update", "Update prospects", "Prospects", defaultPermissions);
        createDefaultPermissionIfNotExists("prospects:delete", "Delete prospects", "Prospects", defaultPermissions);
        
        // Products permissions
        createDefaultPermissionIfNotExists("products:read", "View products", "Products", defaultPermissions);
        createDefaultPermissionIfNotExists("products:create", "Create products", "Products", defaultPermissions);
        createDefaultPermissionIfNotExists("products:update", "Update products", "Products", defaultPermissions);
        createDefaultPermissionIfNotExists("products:delete", "Delete products", "Products", defaultPermissions);
        
        // Quotes permissions
        createDefaultPermissionIfNotExists("quotes:read", "View quotes", "Quotes", defaultPermissions);
        createDefaultPermissionIfNotExists("quotes:create", "Create quotes", "Quotes", defaultPermissions);
        createDefaultPermissionIfNotExists("quotes:update", "Update quotes", "Quotes", defaultPermissions);
        createDefaultPermissionIfNotExists("quotes:delete", "Delete quotes", "Quotes", defaultPermissions);
        
        // Settings permissions
        createDefaultPermissionIfNotExists("settings:read", "View settings", "Settings", defaultPermissions);
        createDefaultPermissionIfNotExists("settings:update", "Update settings", "Settings", defaultPermissions);
        
        // Roles permissions
        createDefaultPermissionIfNotExists("roles:read", "View roles", "Roles", defaultPermissions);
        createDefaultPermissionIfNotExists("roles:create", "Create roles", "Roles", defaultPermissions);
        createDefaultPermissionIfNotExists("roles:update", "Update roles", "Roles", defaultPermissions);
        createDefaultPermissionIfNotExists("roles:delete", "Delete roles", "Roles", defaultPermissions);
        
        // Permissions management
        // Removed permission management permissions
        
        return defaultPermissions;
    }
    
    private void createDefaultPermissionIfNotExists(String name, String description, String category, List<PermissionDto> defaultPermissions) {
        if (!permissionRepository.existsByName(name)) {
            PermissionDto permissionDto = PermissionDto.builder()
                    .name(name)
                    .description(description)
                    .category(category)
                    .build();
            
            PermissionDto savedPermission = save(permissionDto);
            defaultPermissions.add(savedPermission);
        } else {
            Permission existingPermission = permissionRepository.findByName(name)
                    .orElseThrow(() -> new RuntimeException("Permission should exist but was not found: " + name));
            
            defaultPermissions.add(ObjectMapperUtils.mapToDto(existingPermission, PermissionDto.class));
        }
    }
} 