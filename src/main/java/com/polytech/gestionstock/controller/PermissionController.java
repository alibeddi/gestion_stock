package com.polytech.gestionstock.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.polytech.gestionstock.model.dto.PermissionDto;
import com.polytech.gestionstock.model.response.ApiResponse;
import com.polytech.gestionstock.service.PermissionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Permissions", description = "Endpoints for managing permissions")
public class PermissionController {

    private final PermissionService permissionService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Get all permissions",
        description = "Returns a list of all permissions in the system"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Permissions retrieved successfully",
            content = @Content(schema = @Schema(implementation = PermissionDto.class))
        )
    })
    public ResponseEntity<ApiResponse<List<PermissionDto>>> getAllPermissions() {
        log.info("Fetching all permissions");
        
        List<PermissionDto> permissions = permissionService.findAll();
        
        return ResponseEntity.ok(ApiResponse.success(permissions, "Permissions retrieved successfully"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Get permission by ID",
        description = "Returns a specific permission by its ID"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Permission retrieved successfully",
            content = @Content(schema = @Schema(implementation = PermissionDto.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Permission not found"
        )
    })
    public ResponseEntity<ApiResponse<PermissionDto>> getPermissionById(@PathVariable Long id) {
        log.info("Fetching permission with ID: {}", id);
        
        PermissionDto permission = permissionService.findById(id);
        
        return ResponseEntity.ok(ApiResponse.success(permission, "Permission retrieved successfully"));
    }

    @GetMapping("/name/{name}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Get permission by name",
        description = "Returns a specific permission by its name"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Permission retrieved successfully",
            content = @Content(schema = @Schema(implementation = PermissionDto.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Permission not found"
        )
    })
    public ResponseEntity<ApiResponse<PermissionDto>> getPermissionByName(@PathVariable String name) {
        log.info("Fetching permission with name: {}", name);
        
        PermissionDto permission = permissionService.findByName(name);
        
        return ResponseEntity.ok(ApiResponse.success(permission, "Permission retrieved successfully"));
    }

    @GetMapping("/category/{category}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Get permissions by category",
        description = "Returns a list of permissions belonging to a specific category"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Permissions retrieved successfully",
            content = @Content(schema = @Schema(implementation = PermissionDto.class))
        )
    })
    public ResponseEntity<ApiResponse<List<PermissionDto>>> getPermissionsByCategory(@PathVariable String category) {
        log.info("Fetching permissions with category: {}", category);
        
        List<PermissionDto> permissions = permissionService.findByCategory(category);
        
        return ResponseEntity.ok(ApiResponse.success(permissions, "Permissions retrieved successfully"));
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Search permissions",
        description = "Returns a list of permissions containing the specified name"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Permissions retrieved successfully",
            content = @Content(schema = @Schema(implementation = PermissionDto.class))
        )
    })
    public ResponseEntity<ApiResponse<List<PermissionDto>>> searchPermissions(@RequestParam String name) {
        log.info("Searching permissions with name containing: {}", name);
        
        List<PermissionDto> permissions = permissionService.findByNameContaining(name);
        
        return ResponseEntity.ok(ApiResponse.success(permissions, "Permissions retrieved successfully"));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Create permission",
        description = "Creates a new permission"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "Permission created successfully",
            content = @Content(schema = @Schema(implementation = PermissionDto.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Invalid permission data"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "409",
            description = "Permission already exists"
        )
    })
    public ResponseEntity<ApiResponse<PermissionDto>> createPermission(@Valid @RequestBody PermissionDto permissionDto) {
        log.info("Creating new permission with name: {}", permissionDto.getName());
        
        PermissionDto createdPermission = permissionService.save(permissionDto);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(createdPermission, "Permission created successfully"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Update permission",
        description = "Updates an existing permission"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Permission updated successfully",
            content = @Content(schema = @Schema(implementation = PermissionDto.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Invalid permission data"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Permission not found"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "409",
            description = "Permission name already in use"
        )
    })
    public ResponseEntity<ApiResponse<PermissionDto>> updatePermission(
            @PathVariable Long id, 
            @Valid @RequestBody PermissionDto permissionDto) {
        log.info("Updating permission with ID: {}", id);
        
        PermissionDto updatedPermission = permissionService.update(id, permissionDto);
        
        return ResponseEntity.ok(ApiResponse.success(updatedPermission, "Permission updated successfully"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Delete permission",
        description = "Deletes an existing permission"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Permission deleted successfully"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Permission not found"
        )
    })
    public ResponseEntity<ApiResponse<Void>> deletePermission(@PathVariable Long id) {
        log.info("Deleting permission with ID: {}", id);
        
        permissionService.delete(id);
        
        return ResponseEntity.ok(ApiResponse.success(null, "Permission deleted successfully"));
    }

    @PostMapping("/init")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Initialize default permissions",
        description = "Creates default system permissions if they don't exist"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Default permissions initialized successfully",
            content = @Content(schema = @Schema(implementation = PermissionDto.class))
        )
    })
    public ResponseEntity<ApiResponse<List<PermissionDto>>> initDefaultPermissions() {
        log.info("Initializing default permissions");
        
        List<PermissionDto> initializedPermissions = permissionService.initDefaultPermissions();
        
        return ResponseEntity.ok(ApiResponse.success(initializedPermissions, "Default permissions initialized successfully"));
    }
} 