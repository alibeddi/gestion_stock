package com.polytech.gestionstock.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "devis")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Devis {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "numero_devis", unique = true)
    private String numeroDevis;
    
    @Column(name = "sujet")
    private String sujet;
    
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;
    
    @ManyToOne
    @JoinColumn(name = "prospect_id")
    private Prospect prospect;
    
    @Column(name = "echeance")
    private LocalDate echeance;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "delai_livraison")
    private DelaiLivraison delaiLivraison;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "mode_livraison")
    private ModeDeLivraison modeLivraison;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "mode_paiement")
    private ModeDeReglement modePaiement;
    
    @OneToMany(mappedBy = "devis", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LigneDevis> lignesDevis = new ArrayList<>();
    
    @Column(name = "total_ttc")
    private BigDecimal totalTTC;
    
    @Column(name = "total_poids_kg")
    private BigDecimal totalPoidsKg;
    
    @CreatedDate
    @Column(name = "date_creation", updatable = false)
    private LocalDateTime dateCreation;
    
    @LastModifiedDate
    @Column(name = "date_modification")
    private LocalDateTime dateModification;
} 