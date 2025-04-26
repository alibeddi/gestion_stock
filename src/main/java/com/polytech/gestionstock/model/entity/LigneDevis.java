package com.polytech.gestionstock.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "lignes_devis")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LigneDevis {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "devis_id", nullable = false)
    private Devis devis;
    
    @ManyToOne
    @JoinColumn(name = "produit_id", nullable = false)
    private Produit produit;
    
    @Column(name = "quantite", nullable = false)
    private Integer quantite;
    
    @Column(name = "prix_unitaire_ht", nullable = false)
    private BigDecimal prixUnitaireHT;
    
    @Column(name = "taux_tva")
    private BigDecimal tauxTVA = new BigDecimal("0.19");  // Default TVA rate in Tunisia
    
    @Column(name = "montant_tva")
    private BigDecimal montantTVA;
    
    @Column(name = "ecozit")
    private BigDecimal ecozit;
    
    @Column(name = "prix_ttc")
    private BigDecimal prixTTC;
    
    @Column(name = "total_ttc")
    private BigDecimal totalTTC;
    
    @Column(name = "poids_total")
    private BigDecimal poidsTotal;
} 