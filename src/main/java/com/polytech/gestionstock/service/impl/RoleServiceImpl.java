package com.polytech.gestionstock.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.polytech.gestionstock.exception.DuplicateEntityException;
import com.polytech.gestionstock.exception.EntityNotFoundException;
import com.polytech.gestionstock.model.dto.RoleDto;
import com.polytech.gestionstock.model.entity.Role;
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

    @Override
    @Transactional
    public RoleDto save(RoleDto roleDto) {
        log.info("Saving role with name: {}", roleDto.getName());
        
        if (roleRepository.existsByName(roleDto.getName())) {
            throw new DuplicateEntityException("Role", "name", roleDto.getName());
        }
        
        Role role = ObjectMapperUtils.mapToEntity(roleDto, Role.class);
        role = roleRepository.save(role);
        
        return ObjectMapperUtils.mapToDto(role, RoleDto.class);
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
} 