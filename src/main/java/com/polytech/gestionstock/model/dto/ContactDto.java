package com.polytech.gestionstock.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactDto {
    private Long id;
    
    @NotBlank(message = "Le nom est obligatoire")
    private String nom;
    
    private String prenom;
    private String fonction;
    private String nomSociete;
    
    private String mobile;
    private String telephone;
    private String fax;
    
    @Email(message = "Format d'email invalide")
    private String email;
    
    @Email(message = "Format d'email invalide")
    private String emailSecondaire;
    
    private String siteWeb;
    
    private String adresseRue;
    private String adresseCodePostal;
    private String adresseVille;
    private String adressePays;
    
    private Long clientId;
    
    private LocalDateTime dateCreation;
    private LocalDateTime dateModification;
} 