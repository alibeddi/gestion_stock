package com.polytech.gestionstock.controller;

import com.polytech.gestionstock.model.dto.ProspectDto;
import com.polytech.gestionstock.model.entity.StatutProspect;
import com.polytech.gestionstock.model.response.ApiResponse;
import com.polytech.gestionstock.service.ProspectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prospects")
@RequiredArgsConstructor
@Slf4j
public class ProspectController {

    private final ProspectService prospectService;

    @PostMapping
    public ResponseEntity<ApiResponse<ProspectDto>> createProspect(@Valid @RequestBody ProspectDto prospectDto) {
        log.info("Creating new prospect: {}", prospectDto.getNom());
        
        ProspectDto savedProspect = prospectService.save(prospectDto);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(savedProspect, "Prospect created successfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProspectDto>>> getAllProspects() {
        log.info("Fetching all prospects");
        
        List<ProspectDto> prospects = prospectService.findAll();
        
        return ResponseEntity.ok(ApiResponse.success(prospects, "Prospects retrieved successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProspectDto>> getProspectById(@PathVariable Long id) {
        log.info("Fetching prospect with ID: {}", id);
        
        ProspectDto prospect = prospectService.findById(id);
        
        return ResponseEntity.ok(ApiResponse.success(prospect, "Prospect retrieved successfully"));
    }

    @GetMapping("/nom/{nom}")
    public ResponseEntity<ApiResponse<List<ProspectDto>>> getProspectsByNom(@PathVariable String nom) {
        log.info("Fetching prospects with nom containing: {}", nom);
        
        List<ProspectDto> prospects = prospectService.findByNom(nom);
        
        return ResponseEntity.ok(ApiResponse.success(prospects, "Prospects retrieved successfully"));
    }

    @GetMapping("/societe/{societe}")
    public ResponseEntity<ApiResponse<List<ProspectDto>>> getProspectsBySociete(@PathVariable String societe) {
        log.info("Fetching prospects with societe containing: {}", societe);
        
        List<ProspectDto> prospects = prospectService.findBySociete(societe);
        
        return ResponseEntity.ok(ApiResponse.success(prospects, "Prospects retrieved successfully"));
    }

    @GetMapping("/statut/{statut}")
    public ResponseEntity<ApiResponse<List<ProspectDto>>> getProspectsByStatut(@PathVariable StatutProspect statut) {
        log.info("Fetching prospects with statut: {}", statut);
        
        List<ProspectDto> prospects = prospectService.findByStatut(statut);
        
        return ResponseEntity.ok(ApiResponse.success(prospects, "Prospects retrieved successfully"));
    }

    @GetMapping("/secteur-activite/{id}")
    public ResponseEntity<ApiResponse<List<ProspectDto>>> getProspectsBySecteurActivite(@PathVariable Long id) {
        log.info("Fetching prospects by secteur d'activité ID: {}", id);
        
        List<ProspectDto> prospects = prospectService.findBySecteurActiviteId(id);
        
        return ResponseEntity.ok(ApiResponse.success(prospects, "Prospects retrieved successfully"));
    }

    @GetMapping("/source-prospection/{id}")
    public ResponseEntity<ApiResponse<List<ProspectDto>>> getProspectsBySourceProspection(@PathVariable Long id) {
        log.info("Fetching prospects by source de prospection ID: {}", id);
        
        List<ProspectDto> prospects = prospectService.findBySourceProspectionId(id);
        
        return ResponseEntity.ok(ApiResponse.success(prospects, "Prospects retrieved successfully"));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<ApiResponse<List<ProspectDto>>> getProspectsByEmail(@PathVariable String email) {
        log.info("Fetching prospects with email containing: {}", email);
        
        List<ProspectDto> prospects = prospectService.findByEmail(email);
        
        return ResponseEntity.ok(ApiResponse.success(prospects, "Prospects retrieved successfully"));
    }

    @GetMapping("/statut/{statut}/recent")
    public ResponseEntity<ApiResponse<List<ProspectDto>>> getRecentProspectsByStatut(@PathVariable StatutProspect statut) {
        log.info("Fetching recent prospects with statut: {}", statut);
        
        List<ProspectDto> prospects = prospectService.findByStatutOrderByDateCreationDesc(statut);
        
        return ResponseEntity.ok(ApiResponse.success(prospects, "Recent prospects retrieved successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProspectDto>> updateProspect(
            @PathVariable Long id, 
            @Valid @RequestBody ProspectDto prospectDto) {
        log.info("Updating prospect with ID: {}", id);
        
        ProspectDto updatedProspect = prospectService.update(id, prospectDto);
        
        return ResponseEntity.ok(ApiResponse.success(updatedProspect, "Prospect updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProspect(@PathVariable Long id) {
        log.info("Deleting prospect with ID: {}", id);
        
        prospectService.delete(id);
        
        return ResponseEntity.ok(ApiResponse.success(null, "Prospect deleted successfully"));
    }
} 