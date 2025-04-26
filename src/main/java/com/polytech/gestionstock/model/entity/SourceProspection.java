package com.polytech.gestionstock.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sources_prospection")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SourceProspection {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "code", nullable = false, unique = true)
    private String code;
    
    @Column(name = "libelle", nullable = false)
    private String libelle;
    
    @Column(name = "description")
    private String description;
} 