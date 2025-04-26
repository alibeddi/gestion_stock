package com.polytech.gestionstock.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.polytech.gestionstock.model.entity.DelaiLivraison;
import com.polytech.gestionstock.model.entity.ModeDeLivraison;
import com.polytech.gestionstock.model.entity.ModeDeReglement;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DevisDto {
    private Long id;
    
    private String numeroDevis;
    
    @NotBlank(message = "Le sujet est obligatoire")
    private String sujet;
    
    private ClientDto client;
    private ProspectDto prospect;
    
    private LocalDate echeance;
    
    private DelaiLivraison delaiLivraison;
    private ModeDeLivraison modeLivraison;
    private ModeDeReglement modePaiement;
    
    @Builder.Default
    private List<LigneDevisDto> lignesDevis = new ArrayList<>();
    
    private BigDecimal totalTTC;
    private BigDecimal totalPoidsKg;
    
    private LocalDateTime dateCreation;
    private LocalDateTime dateModification;
}
 