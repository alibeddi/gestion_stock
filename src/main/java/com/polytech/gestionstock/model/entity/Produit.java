package com.polytech.gestionstock.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "produits")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Produit {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "code", nullable = false, unique = true)
    private String code;
    
    @Column(name = "libelle", nullable = false)
    private String libelle;
    
    @ManyToOne
    @JoinColumn(name = "emballage_id")
    private Emballage emballage;
    
    @Column(name = "categorie")
    private String categorie;
    
    @Column(name = "poids_kg")
    private BigDecimal poidsKg;
    
    @Column(name = "type_produit")
    private String typeProduit;
    
    @Column(name = "actif")
    private Boolean actif = true;
    
    @Column(name = "package")
    private Boolean isPackage = false;
    
    @Column(name = "ecozit")
    private Boolean ecozit = false;
    
    @Column(name = "prix_gros")
    private BigDecimal prixGros;
    
    @Column(name = "prix_detail")
    private BigDecimal prixDetail;
    
    @Column(name = "prix_gerant")
    private BigDecimal prixGerant;
    
    @CreatedDate
    @Column(name = "date_creation", updatable = false)
    private LocalDateTime dateCreation;
    
    @LastModifiedDate
    @Column(name = "date_modification")
    private LocalDateTime dateModification;
} 