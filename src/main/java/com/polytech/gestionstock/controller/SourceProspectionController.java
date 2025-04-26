package com.polytech.gestionstock.controller;

import com.polytech.gestionstock.model.dto.SourceProspectionDto;
import com.polytech.gestionstock.model.response.ApiResponse;
import com.polytech.gestionstock.service.SourceProspectionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sources-prospection")
@RequiredArgsConstructor
@Slf4j
public class SourceProspectionController {

    private final SourceProspectionService sourceProspectionService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<SourceProspectionDto>> createSourceProspection(@Valid @RequestBody SourceProspectionDto sourceProspectionDto) {
        log.info("Creating new source de prospection: {}", sourceProspectionDto.getCode());
        
        SourceProspectionDto savedSourceProspection = sourceProspectionService.save(sourceProspectionDto);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(savedSourceProspection, "Source de prospection created successfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<SourceProspectionDto>>> getAllSourcesProspection() {
        log.info("Fetching all sources de prospection");
        
        List<SourceProspectionDto> sourcesProspection = sourceProspectionService.findAll();
        
        return ResponseEntity.ok(ApiResponse.success(sourcesProspection, "Sources de prospection retrieved successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SourceProspectionDto>> getSourceProspectionById(@PathVariable Long id) {
        log.info("Fetching source de prospection with ID: {}", id);
        
        SourceProspectionDto sourceProspection = sourceProspectionService.findById(id);
        
        return ResponseEntity.ok(ApiResponse.success(sourceProspection, "Source de prospection retrieved successfully"));
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<ApiResponse<SourceProspectionDto>> getSourceProspectionByCode(@PathVariable String code) {
        log.info("Fetching source de prospection with code: {}", code);
        
        SourceProspectionDto sourceProspection = sourceProspectionService.findByCode(code);
        
        return ResponseEntity.ok(ApiResponse.success(sourceProspection, "Source de prospection retrieved successfully"));
    }

    @GetMapping("/libelle/{libelle}")
    public ResponseEntity<ApiResponse<SourceProspectionDto>> getSourceProspectionByLibelle(@PathVariable String libelle) {
        log.info("Fetching source de prospection with libelle: {}", libelle);
        
        SourceProspectionDto sourceProspection = sourceProspectionService.findByLibelle(libelle);
        
        return ResponseEntity.ok(ApiResponse.success(sourceProspection, "Source de prospection retrieved successfully"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<SourceProspectionDto>> updateSourceProspection(
            @PathVariable Long id, 
            @Valid @RequestBody SourceProspectionDto sourceProspectionDto) {
        log.info("Updating source de prospection with ID: {}", id);
        
        SourceProspectionDto updatedSourceProspection = sourceProspectionService.update(id, sourceProspectionDto);
        
        return ResponseEntity.ok(ApiResponse.success(updatedSourceProspection, "Source de prospection updated successfully"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteSourceProspection(@PathVariable Long id) {
        log.info("Deleting source de prospection with ID: {}", id);
        
        sourceProspectionService.delete(id);
        
        return ResponseEntity.ok(ApiResponse.success(null, "Source de prospection deleted successfully"));
    }
} 