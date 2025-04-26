package com.polytech.gestionstock.model.dto;

import com.polytech.gestionstock.model.entity.StatutProspect;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProspectDto {
    private Long id;
    
    @NotBlank(message = "Le nom est obligatoire")
    private String nom;
    
    private String prenom;
    private String titre;
    private String societe;
    private BigDecimal chiffreAffaires;
    private Integer effectif;
    
    private SecteurActiviteDto secteurActivite;
    private SourceProspectionDto sourceProspection;
    
    private String mobile;
    private String telephone;
    private String fax;
    
    @Email(message = "Format d'email invalide")
    private String email;
    
    @Email(message = "Format d'email invalide")
    private String emailSecondaire;
    
    private String siteWeb;
    
    private StatutProspect statut;
    
    private String adresseRue;
    private String adresseCodePostal;
    private String adresseVille;
    private String adressePays;
    
    private LocalDateTime dateCreation;
    private LocalDateTime dateModification;
} 