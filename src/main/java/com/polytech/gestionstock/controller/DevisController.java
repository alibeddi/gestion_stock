package com.polytech.gestionstock.controller;

import com.polytech.gestionstock.model.dto.DevisDto;
import com.polytech.gestionstock.model.dto.LigneDevisDto;
import com.polytech.gestionstock.model.response.ApiResponse;
import com.polytech.gestionstock.service.DevisService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/devis")
@RequiredArgsConstructor
@Slf4j
public class DevisController {

    private final DevisService devisService;

    @PostMapping
    public ResponseEntity<ApiResponse<DevisDto>> createDevis(@Valid @RequestBody DevisDto devisDto) {
        log.info("Creating new devis for: {}", devisDto.getSujet());
        
        DevisDto savedDevis = devisService.save(devisDto);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(savedDevis, "Devis created successfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DevisDto>>> getAllDevis() {
        log.info("Fetching all devis");
        
        List<DevisDto> devisList = devisService.findAll();
        
        return ResponseEntity.ok(ApiResponse.success(devisList, "Devis retrieved successfully"));
    }

    @GetMapping("/recent")
    public ResponseEntity<ApiResponse<List<DevisDto>>> getRecentDevis() {
        log.info("Fetching recent devis");
        
        List<DevisDto> devisList = devisService.findAllOrderByDateCreationDesc();
        
        return ResponseEntity.ok(ApiResponse.success(devisList, "Recent devis retrieved successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DevisDto>> getDevisById(@PathVariable Long id) {
        log.info("Fetching devis with ID: {}", id);
        
        DevisDto devis = devisService.findById(id);
        
        return ResponseEntity.ok(ApiResponse.success(devis, "Devis retrieved successfully"));
    }

    @GetMapping("/numero/{numeroDevis}")
    public ResponseEntity<ApiResponse<DevisDto>> getDevisByNumero(@PathVariable String numeroDevis) {
        log.info("Fetching devis with numéro: {}", numeroDevis);
        
        DevisDto devis = devisService.findByNumeroDevis(numeroDevis);
        
        return ResponseEntity.ok(ApiResponse.success(devis, "Devis retrieved successfully"));
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<ApiResponse<List<DevisDto>>> getDevisByClient(@PathVariable Long clientId) {
        log.info("Fetching devis for client ID: {}", clientId);
        
        List<DevisDto> devisList = devisService.findByClientId(clientId);
        
        return ResponseEntity.ok(ApiResponse.success(devisList, "Devis retrieved successfully"));
    }

    @GetMapping("/prospect/{prospectId}")
    public ResponseEntity<ApiResponse<List<DevisDto>>> getDevisByProspect(@PathVariable Long prospectId) {
        log.info("Fetching devis for prospect ID: {}", prospectId);
        
        List<DevisDto> devisList = devisService.findByProspectId(prospectId);
        
        return ResponseEntity.ok(ApiResponse.success(devisList, "Devis retrieved successfully"));
    }

    @GetMapping("/sujet/{sujet}")
    public ResponseEntity<ApiResponse<List<DevisDto>>> getDevisBySujet(@PathVariable String sujet) {
        log.info("Fetching devis with sujet containing: {}", sujet);
        
        List<DevisDto> devisList = devisService.findBySujet(sujet);
        
        return ResponseEntity.ok(ApiResponse.success(devisList, "Devis retrieved successfully"));
    }

    @GetMapping("/echeance/{date}")
    public ResponseEntity<ApiResponse<List<DevisDto>>> getDevisByEcheance(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("Fetching devis with echeance: {}", date);
        
        List<DevisDto> devisList = devisService.findByEcheance(date);
        
        return ResponseEntity.ok(ApiResponse.success(devisList, "Devis retrieved successfully"));
    }

    @GetMapping("/echeance/after/{date}")
    public ResponseEntity<ApiResponse<List<DevisDto>>> getDevisByEcheanceAfter(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("Fetching devis with echeance after: {}", date);
        
        List<DevisDto> devisList = devisService.findByEcheanceAfter(date);
        
        return ResponseEntity.ok(ApiResponse.success(devisList, "Devis retrieved successfully"));
    }

    @GetMapping("/echeance/before/{date}")
    public ResponseEntity<ApiResponse<List<DevisDto>>> getDevisByEcheanceBefore(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("Fetching devis with echeance before: {}", date);
        
        List<DevisDto> devisList = devisService.findByEcheanceBefore(date);
        
        return ResponseEntity.ok(ApiResponse.success(devisList, "Devis retrieved successfully"));
    }

    @PostMapping("/{devisId}/lignes")
    public ResponseEntity<ApiResponse<DevisDto>> addLigneDevis(
            @PathVariable Long devisId, 
            @Valid @RequestBody LigneDevisDto ligneDevisDto) {
        log.info("Adding ligne to devis with ID: {}", devisId);
        
        DevisDto updatedDevis = devisService.addLigneDevis(devisId, ligneDevisDto);
        
        return ResponseEntity.ok(ApiResponse.success(updatedDevis, "Ligne added to devis successfully"));
    }

    @DeleteMapping("/{devisId}/lignes/{ligneId}")
    public ResponseEntity<ApiResponse<DevisDto>> removeLigneDevis(
            @PathVariable Long devisId, 
            @PathVariable Long ligneId) {
        log.info("Removing ligne ID {} from devis ID: {}", ligneId, devisId);
        
        DevisDto updatedDevis = devisService.removeLigneDevis(devisId, ligneId);
        
        return ResponseEntity.ok(ApiResponse.success(updatedDevis, "Ligne removed from devis successfully"));
    }

    @PostMapping("/{devisId}/calculate")
    public ResponseEntity<ApiResponse<DevisDto>> calculateTotals(@PathVariable Long devisId) {
        log.info("Calculating totals for devis with ID: {}", devisId);
        
        DevisDto calculatedDevis = devisService.calculateTotals(devisId);
        
        return ResponseEntity.ok(ApiResponse.success(calculatedDevis, "Devis totals calculated successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DevisDto>> updateDevis(
            @PathVariable Long id, 
            @Valid @RequestBody DevisDto devisDto) {
        log.info("Updating devis with ID: {}", id);
        
        DevisDto updatedDevis = devisService.update(id, devisDto);
        
        return ResponseEntity.ok(ApiResponse.success(updatedDevis, "Devis updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteDevis(@PathVariable Long id) {
        log.info("Deleting devis with ID: {}", id);
        
        devisService.delete(id);
        
        return ResponseEntity.ok(ApiResponse.success(null, "Devis deleted successfully"));
    }
} 