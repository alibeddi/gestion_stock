package com.polytech.gestionstock.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payment_modes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMode {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "name", nullable = false, unique = true)
    private String name;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "code", nullable = false, unique = true)
    private String code;
    
    @Column(name = "active", nullable = false)
    private boolean active = true;
    
    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;
} 