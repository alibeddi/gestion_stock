package com.polytech.gestionstock.controller;

import java.util.List;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.polytech.gestionstock.model.dto.PermissionDto;
import com.polytech.gestionstock.model.dto.UserDto;
import com.polytech.gestionstock.model.response.ApiResponse;
import com.polytech.gestionstock.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Users", description = "Endpoints for managing users and their permissions")
public class UserController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<UserDto>>> getAllUsers() {
        log.info("Fetching all users");
        
        List<UserDto> users = userService.findAll();
        
        return ResponseEntity.ok(ApiResponse.success(users, "Users retrieved successfully"));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserDto>> createUser(@Valid @RequestBody UserDto userDto) {
        log.info("Creating new user with email: {} and role: {}", userDto.getEmail(), userDto.getRole());
        
        UserDto createdUser = userService.save(userDto);
        
        return ResponseEntity.ok(ApiResponse.success(createdUser, "User created successfully"));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserDto>> getCurrentUser() {
        log.info("Fetching current user");
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();
        
        UserDto user = userService.findByEmail(currentUserEmail);
        
        return ResponseEntity.ok(ApiResponse.success(user, "Current user retrieved successfully"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityUtils.isCurrentUser(#id)")
    public ResponseEntity<ApiResponse<UserDto>> getUserById(@PathVariable Long id) {
        log.info("Fetching user with ID: {}", id);
        
        UserDto user = userService.findById(id);
        
        return ResponseEntity.ok(ApiResponse.success(user, "User retrieved successfully"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityUtils.isCurrentUser(#id)")
    public ResponseEntity<ApiResponse<UserDto>> updateUser(@PathVariable Long id, @Valid @RequestBody UserDto userDto) {
        log.info("Updating user with ID: {}", id);
        
        UserDto updatedUser = userService.update(id, userDto);
        
        return ResponseEntity.ok(ApiResponse.success(updatedUser, "User updated successfully"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        log.info("Deleting user with ID: {}", id);
        
        userService.delete(id);
        
        return ResponseEntity.ok(ApiResponse.success(null, "User deleted successfully"));
    }

    @GetMapping("/{id}/permissions")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Get user permissions",
        description = "Returns all permissions associated with a user (direct and from roles)"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Permissions retrieved successfully",
            content = @Content(schema = @Schema(implementation = PermissionDto.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "User not found"
        )
    })
    public ResponseEntity<ApiResponse<Set<PermissionDto>>> getUserPermissions(@PathVariable Long id) {
        log.info("Fetching permissions for user with ID: {}", id);
        
        Set<PermissionDto> permissions = userService.getUserPermissions(id);
        
        return ResponseEntity.ok(ApiResponse.success(permissions, "User permissions retrieved successfully"));
    }

    @PostMapping("/{id}/permissions")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Assign permissions to user",
        description = "Assigns direct permissions to a user"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Permissions assigned successfully",
            content = @Content(schema = @Schema(implementation = UserDto.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "User or permission not found"
        )
    })
    public ResponseEntity<ApiResponse<UserDto>> assignPermissionsToUser(
            @PathVariable Long id, 
            @RequestBody Set<Long> permissionIds) {
        log.info("Assigning permissions {} to user with ID: {}", permissionIds, id);
        
        UserDto updatedUser = userService.assignPermissionsToUser(id, permissionIds);
        
        return ResponseEntity.ok(ApiResponse.success(updatedUser, "Permissions assigned successfully"));
    }

    @DeleteMapping("/{id}/permissions")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Remove permissions from user",
        description = "Removes direct permissions from a user"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Permissions removed successfully",
            content = @Content(schema = @Schema(implementation = UserDto.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "User not found"
        )
    })
    public ResponseEntity<ApiResponse<UserDto>> removePermissionsFromUser(
            @PathVariable Long id, 
            @RequestBody Set<Long> permissionIds) {
        log.info("Removing permissions {} from user with ID: {}", permissionIds, id);
        
        UserDto updatedUser = userService.removePermissionsFromUser(id, permissionIds);
        
        return ResponseEntity.ok(ApiResponse.success(updatedUser, "Permissions removed successfully"));
    }
} 