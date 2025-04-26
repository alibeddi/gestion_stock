package com.polytech.gestionstock.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProduitDto {
    private Long id;
    
    @NotBlank(message = "Le code est obligatoire")
    private String code;
    
    @NotBlank(message = "Le libellé est obligatoire")
    private String libelle;
    
    private EmballageDto emballage;
    
    private String categorie;
    private BigDecimal poidsKg;
    private String typeProduit;
    
    @Builder.Default
    private Boolean actif = true;
    
    @Builder.Default
    private Boolean isPackage = false;
    
    @Builder.Default
    private Boolean ecozit = false;
    
    @NotNull(message = "Le prix de gros est obligatoire")
    private BigDecimal prixGros;
    
    @NotNull(message = "Le prix de détail est obligatoire")
    private BigDecimal prixDetail;
    
    @NotNull(message = "Le prix gérant est obligatoire")
    private BigDecimal prixGerant;
    
    private LocalDateTime dateCreation;
    private LocalDateTime dateModification;
} 