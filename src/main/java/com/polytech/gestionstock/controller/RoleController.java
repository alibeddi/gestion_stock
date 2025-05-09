package com.polytech.gestionstock.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.polytech.gestionstock.model.dto.RoleDto;
import com.polytech.gestionstock.model.response.ApiResponse;
import com.polytech.gestionstock.service.RoleService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('ADMIN')")
public class RoleController {

    private final RoleService roleService;

    @PostMapping
    public ResponseEntity<ApiResponse<RoleDto>> createRole(@Valid @RequestBody RoleDto roleDto) {
        log.info("Creating new role: {}", roleDto.getName());
        
        RoleDto savedRole = roleService.save(roleDto);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(savedRole, "Role created successfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<RoleDto>>> getAllRoles() {
        log.info("Fetching all roles");
        
        List<RoleDto> roles = roleService.findAll();
        
        return ResponseEntity.ok(ApiResponse.success(roles, "Roles retrieved successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RoleDto>> getRoleById(@PathVariable Long id) {
        log.info("Fetching role with ID: {}", id);
        
        RoleDto role = roleService.findById(id);
        
        return ResponseEntity.ok(ApiResponse.success(role, "Role retrieved successfully"));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ApiResponse<RoleDto>> getRoleByName(@PathVariable String name) {
        log.info("Fetching role with name: {}", name);
        
        RoleDto role = roleService.findByName(name);
        
        return ResponseEntity.ok(ApiResponse.success(role, "Role retrieved successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteRole(@PathVariable Long id) {
        log.info("Deleting role with ID: {}", id);
        
        roleService.delete(id);
        
        return ResponseEntity.ok(ApiResponse.success(null, "Role deleted successfully"));
    }
} 