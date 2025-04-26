package com.polytech.gestionstock.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "contacts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Contact {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "nom", nullable = false)
    private String nom;
    
    @Column(name = "prenom")
    private String prenom;
    
    @Column(name = "fonction")
    private String fonction;
    
    @Column(name = "nom_societe")
    private String nomSociete;
    
    @Column(name = "mobile")
    private String mobile;
    
    @Column(name = "telephone")
    private String telephone;
    
    @Column(name = "fax")
    private String fax;
    
    @Column(name = "email")
    private String email;
    
    @Column(name = "email_secondaire")
    private String emailSecondaire;
    
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
    
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;
    
    @CreatedDate
    @Column(name = "date_creation", updatable = false)
    private LocalDateTime dateCreation;
    
    @LastModifiedDate
    @Column(name = "date_modification")
    private LocalDateTime dateModification;
} 