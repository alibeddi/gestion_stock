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
@Table(name = "clients")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Client {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "nom", nullable = false)
    private String nom;
    
    @Column(name = "numero_compte", unique = true)
    private String numeroCompte;
    
    @Column(name = "numero_sous_compte")
    private String numeroSousCompte;
    
    @Column(name = "matricule_fiscal", unique = true)
    private String matriculeFiscal;
    
    @Column(name = "chiffre_affaires")
    private BigDecimal chiffreAffaires;
    
    @Column(name = "effectif")
    private Integer effectif;
    
    @ManyToOne
    @JoinColumn(name = "secteur_activite_id")
    private SecteurActivite secteurActivite;
    
    @Column(name = "exonere")
    private Boolean exonere = false;
    
    @Column(name = "date_limite_exoneration")
    private LocalDate dateLimiteExoneration;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "mode_reglement")
    private ModeReglement modeReglement;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "prix_achat")
    private PrixAchat prixAchat;
    
    @ManyToOne
    @JoinColumn(name = "gouvernorat_id")
    private Gouvernorat gouvernorat;
    
    @Column(name = "mobile")
    private String mobile;
    
    @Column(name = "telephone")
    private String telephone;
    
    @Column(name = "autre_telephone")
    private String autreTelephone;
    
    @Column(name = "fax")
    private String fax;
    
    @Column(name = "email")
    private String email;
    
    @Column(name = "autre_email")
    private String autreEmail;
    
    @Column(name = "site_web")
    private String siteWeb;
    
    @Column(name = "adresse_rue")
    private String adresseRue;
    
    @Column(name = "adresse_code_postal")
    private String adresseCodePostal;
    
    @Column(name = "adresse_ville")
    private String adresseVille;
    
    @Column(name = "adresse_pays")
    private String adressePays;
    
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Contact> contacts = new ArrayList<>();
    
    @CreatedDate
    @Column(name = "date_creation", updatable = false)
    private LocalDateTime dateCreation;
    
    @LastModifiedDate
    @Column(name = "date_modification")
    private LocalDateTime dateModification;
} 