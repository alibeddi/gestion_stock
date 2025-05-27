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
public class PermissionDto {
    private Long id;
    
    @NotBlank(message = "Le nom de la permission est obligatoire")
    private String name;
    
    private String description;
    
    private String category;
} 