package com.polytech.gestionstock.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.polytech.gestionstock.model.entity.ModeReglement;
import com.polytech.gestionstock.model.entity.PrixAchat;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientDto {
    private Long id;
    
    @NotBlank(message = "Le nom est obligatoire")
    private String nom;
    
    private String numeroCompte;
    private String numeroSousCompte;
    private String matriculeFiscal;
    
    private BigDecimal chiffreAffaires;
    private Integer effectif;
    
    private SecteurActiviteDto secteurActivite;
    
    @Builder.Default
    private Boolean exonere = false;
    private LocalDate dateLimiteExoneration;
    
    private ModeReglement modeReglement;
    private PrixAchat prixAchat;
    
    private GouvernoratDto gouvernorat;
    
    private String mobile;
    private String telephone;
    private String autreTelephone;
    private String fax;
    
    @Email(message = "Format d'email invalide")
    private String email;
    
    @Email(message = "Format d'email invalide")
    private String autreEmail;
    
    private String siteWeb;
    
    private String adresseRue;
    private String adresseCodePostal;
    private String adresseVille;
    private String adressePays;
    
    @Builder.Default
    private List<ContactDto> contacts = new ArrayList<>();
    
    private LocalDateTime dateCreation;
    private LocalDateTime dateModification;
} 