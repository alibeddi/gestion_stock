package com.polytech.gestionstock.controller;

import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.polytech.gestionstock.model.dto.PermissionDto;
import com.polytech.gestionstock.model.dto.RoleDto;
import com.polytech.gestionstock.model.response.ApiResponse;
import com.polytech.gestionstock.service.RoleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Roles", description = "Endpoints for managing roles and permissions")
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Get all roles",
        description = "Returns a list of all roles in the system"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Roles retrieved successfully",
            content = @Content(schema = @Schema(implementation = RoleDto.class))
        )
    })
    public ResponseEntity<ApiResponse<List<RoleDto>>> getAllRoles() {
        log.info("Fetching all roles");
        
        List<RoleDto> roles = roleService.findAll();
        
        return ResponseEntity.ok(ApiResponse.success(roles, "Roles retrieved successfully"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Get role by ID",
        description = "Returns a specific role by its ID"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Role retrieved successfully",
            content = @Content(schema = @Schema(implementation = RoleDto.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Role not found"
        )
    })
    public ResponseEntity<ApiResponse<RoleDto>> getRoleById(@PathVariable Long id) {
        log.info("Fetching role with ID: {}", id);
        
        RoleDto role = roleService.findById(id);
        
        return ResponseEntity.ok(ApiResponse.success(role, "Role retrieved successfully"));
    }

    @GetMapping("/name/{name}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Get role by name",
        description = "Returns a specific role by its name"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Role retrieved successfully",
            content = @Content(schema = @Schema(implementation = RoleDto.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Role not found"
        )
    })
    public ResponseEntity<ApiResponse<RoleDto>> getRoleByName(@PathVariable String name) {
        log.info("Fetching role with name: {}", name);
        
        RoleDto role = roleService.findByName(name);
        
        return ResponseEntity.ok(ApiResponse.success(role, "Role retrieved successfully"));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Create role",
        description = "Creates a new role"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "Role created successfully",
            content = @Content(schema = @Schema(implementation = RoleDto.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Invalid role data"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "409",
            description = "Role name already in use"
        )
    })
    public ResponseEntity<ApiResponse<RoleDto>> createRole(@Valid @RequestBody RoleDto roleDto) {
        log.info("Creating new role with name: {}", roleDto.getName());
        
        RoleDto createdRole = roleService.save(roleDto);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(createdRole, "Role created successfully"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Update role",
        description = "Updates an existing role"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Role updated successfully",
            content = @Content(schema = @Schema(implementation = RoleDto.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Invalid role data"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Role not found"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "409",
            description = "Role name already in use"
        )
    })
    public ResponseEntity<ApiResponse<RoleDto>> updateRole(@PathVariable Long id, @Valid @RequestBody RoleDto roleDto) {
        log.info("Updating role with ID: {}", id);
        
        RoleDto updatedRole = roleService.update(id, roleDto);
        
        return ResponseEntity.ok(ApiResponse.success(updatedRole, "Role updated successfully"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Delete role",
        description = "Deletes an existing role"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Role deleted successfully"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Role not found"
        )
    })
    public ResponseEntity<ApiResponse<Void>> deleteRole(@PathVariable Long id) {
        log.info("Deleting role with ID: {}", id);
        
        roleService.delete(id);
        
        return ResponseEntity.ok(ApiResponse.success(null, "Role deleted successfully"));
    }
    
    @GetMapping("/{id}/permissions")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Get permissions by role ID",
        description = "Returns a list of permissions associated with a specific role"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Permissions retrieved successfully",
            content = @Content(schema = @Schema(implementation = PermissionDto.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Role not found"
        )
    })
    public ResponseEntity<ApiResponse<Set<PermissionDto>>> getPermissionsByRoleId(@PathVariable Long id) {
        log.info("Fetching permissions for role with ID: {}", id);
        
        Set<PermissionDto> permissions = roleService.getPermissionsByRoleId(id);
        
        return ResponseEntity.ok(ApiResponse.success(permissions, "Permissions retrieved successfully"));
    }
    
    @PostMapping("/{id}/permissions")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Assign permissions to role",
        description = "Assigns a set of permissions to a specific role"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Permissions assigned successfully",
            content = @Content(schema = @Schema(implementation = RoleDto.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Role or permission not found"
        )
    })
    public ResponseEntity<ApiResponse<RoleDto>> assignPermissionsToRole(
            @PathVariable Long id, 
            @RequestBody Set<Long> permissionIds) {
        log.info("Assigning permissions {} to role with ID: {}", permissionIds, id);
        
        RoleDto updatedRole = roleService.assignPermissionsToRole(id, permissionIds);
        
        return ResponseEntity.ok(ApiResponse.success(updatedRole, "Permissions assigned successfully"));
    }
    
    @DeleteMapping("/{id}/permissions")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Remove permissions from role",
        description = "Removes a set of permissions from a specific role"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Permissions removed successfully",
            content = @Content(schema = @Schema(implementation = RoleDto.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Role not found"
        )
    })
    public ResponseEntity<ApiResponse<RoleDto>> removePermissionsFromRole(
            @PathVariable Long id, 
            @RequestBody Set<Long> permissionIds) {
        log.info("Removing permissions {} from role with ID: {}", permissionIds, id);
        
        RoleDto updatedRole = roleService.removePermissionsFromRole(id, permissionIds);
        
        return ResponseEntity.ok(ApiResponse.success(updatedRole, "Permissions removed successfully"));
    }
} 