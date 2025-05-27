package com.polytech.gestionstock.model.dto;

import java.util.HashSet;
import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    
    @NotBlank(message = "Le nom est obligatoire")
    private String nom;
    
    @NotBlank(message = "Le prénom est obligatoire")
    private String prenom;
    
    private String matricule;
    
    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 6, message = "Le mot de passe doit contenir au moins 6 caractères")
    private String password;
    
    private String confirmPassword;
    
    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Format d'email invalide")
    private String email;
    
    private String telephone;
    private String mobile;
    private String titre;
    private String adresse;
    
    // Field for simple role assignment (e.g. "ROLE_USER" or "USER")
    private String role;
    
    @Builder.Default
    private Set<RoleDto> roles = new HashSet<>();
    
    @Builder.Default
    private boolean accountNonExpired = true;
    
    @Builder.Default
    private boolean accountNonLocked = true;
    
    @Builder.Default
    private boolean credentialsNonExpired = true;
    
    @Builder.Default
    private boolean enabled = true;
    
    @Builder.Default
    private Set<PermissionDto> permissions = new HashSet<>();
} 