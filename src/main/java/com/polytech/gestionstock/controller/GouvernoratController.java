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

import com.polytech.gestionstock.model.dto.GouvernoratDto;
import com.polytech.gestionstock.model.response.ApiResponse;
import com.polytech.gestionstock.service.GouvernoratService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/gouvernorats")
@RequiredArgsConstructor
@Slf4j
public class GouvernoratController {

    private final GouvernoratService gouvernoratService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<GouvernoratDto>> createGouvernorat(@Valid @RequestBody GouvernoratDto gouvernoratDto) {
        log.info("Creating new gouvernorat: {}", gouvernoratDto.getCode());
        
        GouvernoratDto savedGouvernorat = gouvernoratService.save(gouvernoratDto);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(savedGouvernorat, "Gouvernorat created successfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<GouvernoratDto>>> getAllGouvernorats() {
        log.info("Fetching all gouvernorats");
        
        List<GouvernoratDto> gouvernorats = gouvernoratService.findAll();
        
        return ResponseEntity.ok(ApiResponse.success(gouvernorats, "Gouvernorats retrieved successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<GouvernoratDto>> getGouvernoratById(@PathVariable Long id) {
        log.info("Fetching gouvernorat with ID: {}", id);
        
        GouvernoratDto gouvernorat = gouvernoratService.findById(id);
        
        return ResponseEntity.ok(ApiResponse.success(gouvernorat, "Gouvernorat retrieved successfully"));
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<ApiResponse<GouvernoratDto>> getGouvernoratByCode(@PathVariable String code) {
        log.info("Fetching gouvernorat with code: {}", code);
        
        GouvernoratDto gouvernorat = gouvernoratService.findByCode(code);
        
        return ResponseEntity.ok(ApiResponse.success(gouvernorat, "Gouvernorat retrieved successfully"));
    }

    @GetMapping("/pays/{pays}")
    public ResponseEntity<ApiResponse<List<GouvernoratDto>>> getGouvernoratsByPays(@PathVariable String pays) {
        log.info("Fetching gouvernorats for pays: {}", pays);
        
        List<GouvernoratDto> gouvernorats = gouvernoratService.findByPays(pays);
        
        return ResponseEntity.ok(ApiResponse.success(gouvernorats, "Gouvernorats retrieved successfully"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<GouvernoratDto>> updateGouvernorat(
            @PathVariable Long id, 
            @Valid @RequestBody GouvernoratDto gouvernoratDto) {
        log.info("Updating gouvernorat with ID: {}", id);
        
        GouvernoratDto updatedGouvernorat = gouvernoratService.update(id, gouvernoratDto);
        
        return ResponseEntity.ok(ApiResponse.success(updatedGouvernorat, "Gouvernorat updated successfully"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteGouvernorat(@PathVariable Long id) {
        log.info("Deleting gouvernorat with ID: {}", id);
        
        gouvernoratService.delete(id);
        
        return ResponseEntity.ok(ApiResponse.success(null, "Gouvernorat deleted successfully"));
    }
} 