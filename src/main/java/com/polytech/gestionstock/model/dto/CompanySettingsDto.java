package com.polytech.gestionstock.model.dto;

import java.time.LocalDateTime;

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
public class CompanySettingsDto {
    private Long id;
    
    @NotBlank(message = "Le nom de l'entreprise est obligatoire")
    private String companyName;
    
    @NotBlank(message = "Le numéro d'identification fiscale est obligatoire")
    private String taxId;
    
    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Format d'email invalide")
    private String email;
    
    @NotBlank(message = "Le numéro de téléphone est obligatoire")
    private String phone;
    
    @NotBlank(message = "L'adresse est obligatoire")
    private String address;
    
    private String postalCode;
    
    @NotBlank(message = "La ville est obligatoire")
    private String city;
    
    @NotBlank(message = "Le pays est obligatoire")
    private String country;
    
    private String website;
    
    private String logoUrl;
    
    private String currency;
    
    private LocalDateTime dateCreation;
    private LocalDateTime dateModification;
} 