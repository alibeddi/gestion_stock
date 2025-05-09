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

import com.polytech.gestionstock.model.dto.SecteurActiviteDto;
import com.polytech.gestionstock.model.response.ApiResponse;
import com.polytech.gestionstock.service.SecteurActiviteService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/secteurs-activite")
@RequiredArgsConstructor
@Slf4j
public class SecteurActiviteController {

    private final SecteurActiviteService secteurActiviteService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<SecteurActiviteDto>> createSecteurActivite(@Valid @RequestBody SecteurActiviteDto secteurActiviteDto) {
        log.info("Creating new secteur d'activité: {}", secteurActiviteDto.getCode());
        
        SecteurActiviteDto savedSecteurActivite = secteurActiviteService.save(secteurActiviteDto);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(savedSecteurActivite, "Secteur d'activité created successfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<SecteurActiviteDto>>> getAllSecteursActivite() {
        log.info("Fetching all secteurs d'activité");
        
        List<SecteurActiviteDto> secteursActivite = secteurActiviteService.findAll();
        
        return ResponseEntity.ok(ApiResponse.success(secteursActivite, "Secteurs d'activité retrieved successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SecteurActiviteDto>> getSecteurActiviteById(@PathVariable Long id) {
        log.info("Fetching secteur d'activité with ID: {}", id);
        
        SecteurActiviteDto secteurActivite = secteurActiviteService.findById(id);
        
        return ResponseEntity.ok(ApiResponse.success(secteurActivite, "Secteur d'activité retrieved successfully"));
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<ApiResponse<SecteurActiviteDto>> getSecteurActiviteByCode(@PathVariable String code) {
        log.info("Fetching secteur d'activité with code: {}", code);
        
        SecteurActiviteDto secteurActivite = secteurActiviteService.findByCode(code);
        
        return ResponseEntity.ok(ApiResponse.success(secteurActivite, "Secteur d'activité retrieved successfully"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<SecteurActiviteDto>> updateSecteurActivite(
            @PathVariable Long id, 
            @Valid @RequestBody SecteurActiviteDto secteurActiviteDto) {
        log.info("Updating secteur d'activité with ID: {}", id);
        
        SecteurActiviteDto updatedSecteurActivite = secteurActiviteService.update(id, secteurActiviteDto);
        
        return ResponseEntity.ok(ApiResponse.success(updatedSecteurActivite, "Secteur d'activité updated successfully"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteSecteurActivite(@PathVariable Long id) {
        log.info("Deleting secteur d'activité with ID: {}", id);
        
        secteurActiviteService.delete(id);
        
        return ResponseEntity.ok(ApiResponse.success(null, "Secteur d'activité deleted successfully"));
    }
} 