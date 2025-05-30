package com.polytech.gestionstock.model.dto;

import java.util.HashSet;
import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleDto {
    private Long id;
    
    @NotBlank(message = "Le nom du rôle est obligatoire")
    private String name;
    
    private String libelle;
    
    @Builder.Default
    private Set<PermissionDto> permissions = new HashSet<>();
} 