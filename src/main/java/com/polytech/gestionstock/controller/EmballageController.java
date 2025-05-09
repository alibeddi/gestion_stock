package com.polytech.gestionstock.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.polytech.gestionstock.model.dto.EmballageDto;
import com.polytech.gestionstock.model.response.ApiResponse;
import com.polytech.gestionstock.service.EmballageService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/emballages")
@RequiredArgsConstructor
@Slf4j
public class EmballageController {

    private final EmballageService emballageService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<EmballageDto>> createEmballage(@Valid @RequestBody EmballageDto emballageDto) {
        log.info("Creating new emballage: {}", emballageDto.getCode());
        
        EmballageDto savedEmballage = emballageService.save(emballageDto);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(savedEmballage, "Emballage created successfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<EmballageDto>>> getAllEmballages() {
        log.info("Fetching all emballages");
        
        List<EmballageDto> emballages = emballageService.findAll();
        
        return ResponseEntity.ok(ApiResponse.success(emballages, "Emballages retrieved successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EmballageDto>> getEmballageById(@PathVariable Long id) {
        log.info("Fetching emballage with ID: {}", id);
        
        EmballageDto emballage = emballageService.findById(id);
        
        return ResponseEntity.ok(ApiResponse.success(emballage, "Emballage retrieved successfully"));
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<ApiResponse<EmballageDto>> getEmballageByCode(@PathVariable String code) {
        log.info("Fetching emballage with code: {}", code);
        
        EmballageDto emballage = emballageService.findByCode(code);
        
        return ResponseEntity.ok(ApiResponse.success(emballage, "Emballage retrieved successfully"));
    }

    @GetMapping("/libelle/{libelle}")
    public ResponseEntity<ApiResponse<List<EmballageDto>>> getEmballagesByLibelle(@PathVariable String libelle) {
        log.info("Fetching emballages with libelle containing: {}", libelle);
        
        List<EmballageDto> emballages = emballageService.findByLibelle(libelle);
        
        return ResponseEntity.ok(ApiResponse.success(emballages, "Emballages retrieved successfully"));
    }

    @GetMapping("/type/{typeEmballage}")
    public ResponseEntity<ApiResponse<List<EmballageDto>>> getEmballagesByType(@PathVariable String typeEmballage) {
        log.info("Fetching emballages with type: {}", typeEmballage);
        
        List<EmballageDto> emballages = emballageService.findByTypeEmballage(typeEmballage);
        
        return ResponseEntity.ok(ApiResponse.success(emballages, "Emballages retrieved successfully"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<EmballageDto>> updateEmballage(
            @PathVariable Long id, 
            @Valid @RequestBody EmballageDto emballageDto) {
        log.info("Updating emballage with ID: {}", id);
        
        EmballageDto updatedEmballage = emballageService.update(id, emballageDto);
        
        return ResponseEntity.ok(ApiResponse.success(updatedEmballage, "Emballage updated successfully"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Void>> deleteEmballage(@PathVariable Long id) {
        log.info("Deleting emballage with ID: {}", id);
        
        emballageService.delete(id);
        
        return ResponseEntity.ok(ApiResponse.success(null, "Emballage deleted successfully"));
    }
} 