package com.polytech.gestionstock.model.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LigneDevisDto {
    private Long id;
    
    private Long devisId;
    
    @NotNull(message = "Le produit est obligatoire")
    private ProduitDto produit;
    
    @NotNull(message = "La quantité est obligatoire")
    @Min(value = 1, message = "La quantité minimale est 1")
    private Integer quantite;
    
    @NotNull(message = "Le prix unitaire HT est obligatoire")
    private BigDecimal prixUnitaireHT;
    
    @Builder.Default
    private BigDecimal tauxTVA = new BigDecimal("0.19");
    private BigDecimal montantTVA;
    private BigDecimal ecozit;
    private BigDecimal prixTTC;
    private BigDecimal totalTTC;
    private BigDecimal poidsTotal;
} 