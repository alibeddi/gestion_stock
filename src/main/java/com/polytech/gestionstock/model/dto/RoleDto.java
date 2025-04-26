package com.polytech.gestionstock.model.dto;

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
} 