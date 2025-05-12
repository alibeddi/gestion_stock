package com.polytech.gestionstock.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.polytech.gestionstock.model.dto.ClientDto;
import com.polytech.gestionstock.model.dto.ProspectDto;
import com.polytech.gestionstock.model.response.ApiResponse;
import com.polytech.gestionstock.service.ClientService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
@Slf4j
public class ClientController {

    private final ClientService clientService;

    @PostMapping
    public ResponseEntity<ApiResponse<ClientDto>> createClient(@Valid @RequestBody ClientDto clientDto) {
        log.info("Creating new client: {}", clientDto.getNom());
        
        ClientDto savedClient = clientService.save(clientDto);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(savedClient, "Client created successfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ClientDto>>> getAllClients(
            @RequestParam(required = false) String nom,
            @RequestParam(required = false) String matriculeFiscal,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Long secteurActiviteId,
            @RequestParam(required = false) String sourceProspection,
            @RequestParam(required = false) String statut,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        log.info("Fetching all clients with filters - nom: {}, matriculeFiscal: {}, email: {}, secteurActiviteId: {}, page: {}, size: {}", 
                 nom, matriculeFiscal, email, secteurActiviteId, page, size);
        
        // Check if any filters are applied
        if (nom != null || matriculeFiscal != null || email != null || secteurActiviteId != null 
                || sourceProspection != null || statut != null) {
            
            return ResponseEntity.ok(ApiResponse.success(
                clientService.findWithFilters(nom, matriculeFiscal, email, secteurActiviteId, 
                                            sourceProspection, statut, page, size),
                "Filtered clients retrieved successfully"));
        }
        
        // No filters, return all clients
        List<ClientDto> clients = clientService.findAll();
        return ResponseEntity.ok(ApiResponse.success(clients, "Clients retrieved successfully"));
    }

    @GetMapping("/recent")
    public ResponseEntity<ApiResponse<List<ClientDto>>> getRecentClients() {
        log.info("Fetching recent clients");
        
        List<ClientDto> clients = clientService.findAllOrderByDateCreationDesc();
        
        return ResponseEntity.ok(ApiResponse.success(clients, "Recent clients retrieved successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ClientDto>> getClientById(@PathVariable Long id) {
        log.info("Fetching client with ID: {}", id);
        
        ClientDto client = clientService.findById(id);
        
        return ResponseEntity.ok(ApiResponse.success(client, "Client retrieved successfully"));
    }

    @GetMapping("/numero-compte/{numeroCompte}")
    public ResponseEntity<ApiResponse<ClientDto>> getClientByNumeroCompte(@PathVariable String numeroCompte) {
        log.info("Fetching client with numéro de compte: {}", numeroCompte);
        
        ClientDto client = clientService.findByNumeroCompte(numeroCompte);
        
        return ResponseEntity.ok(ApiResponse.success(client, "Client retrieved successfully"));
    }

    @GetMapping("/matricule-fiscal/{matriculeFiscal}")
    public ResponseEntity<ApiResponse<ClientDto>> getClientByMatriculeFiscal(@PathVariable String matriculeFiscal) {
        log.info("Fetching client with matricule fiscal: {}", matriculeFiscal);
        
        ClientDto client = clientService.findByMatriculeFiscal(matriculeFiscal);
        
        return ResponseEntity.ok(ApiResponse.success(client, "Client retrieved successfully"));
    }

    @GetMapping("/nom/{nom}")
    public ResponseEntity<ApiResponse<List<ClientDto>>> getClientsByNom(@PathVariable String nom) {
        log.info("Fetching clients with nom containing: {}", nom);
        
        List<ClientDto> clients = clientService.findByNom(nom);
        
        return ResponseEntity.ok(ApiResponse.success(clients, "Clients retrieved successfully"));
    }

    @GetMapping("/secteur-activite/{id}")
    public ResponseEntity<ApiResponse<List<ClientDto>>> getClientsBySecteurActivite(@PathVariable Long id) {
        log.info("Fetching clients by secteur d'activité ID: {}", id);
        
        List<ClientDto> clients = clientService.findBySecteurActiviteId(id);
        
        return ResponseEntity.ok(ApiResponse.success(clients, "Clients retrieved successfully"));
    }

    @GetMapping("/gouvernorat/{id}")
    public ResponseEntity<ApiResponse<List<ClientDto>>> getClientsByGouvernorat(@PathVariable Long id) {
        log.info("Fetching clients by gouvernorat ID: {}", id);
        
        List<ClientDto> clients = clientService.findByGouvernoratId(id);
        
        return ResponseEntity.ok(ApiResponse.success(clients, "Clients retrieved successfully"));
    }

    @GetMapping("/exonere/{exonere}")
    public ResponseEntity<ApiResponse<List<ClientDto>>> getClientsByExonere(@PathVariable Boolean exonere) {
        log.info("Fetching clients by exonere: {}", exonere);
        
        List<ClientDto> clients = clientService.findByExonere(exonere);
        
        return ResponseEntity.ok(ApiResponse.success(clients, "Clients retrieved successfully"));
    }

    @GetMapping("/count")
    public ResponseEntity<ApiResponse<Long>> countAllClients() {
        log.info("Counting all clients");
        
        long count = clientService.countAll();
        
        return ResponseEntity.ok(ApiResponse.success(count, "Clients counted successfully"));
    }

    @PostMapping("/convert-prospect")
    public ResponseEntity<ApiResponse<ClientDto>> convertProspectToClient(@Valid @RequestBody ProspectDto prospectDto) {
        log.info("Converting prospect to client: {}", prospectDto.getNom());
        
        ClientDto convertedClient = clientService.convertProspectToClient(prospectDto);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(convertedClient, "Prospect converted to client successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ClientDto>> updateClient(
            @PathVariable Long id, 
            @Valid @RequestBody ClientDto clientDto) {
        log.info("Updating client with ID: {}", id);
        
        ClientDto updatedClient = clientService.update(id, clientDto);
        
        return ResponseEntity.ok(ApiResponse.success(updatedClient, "Client updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteClient(@PathVariable Long id) {
        log.info("Deleting client with ID: {}", id);
        
        clientService.delete(id);
        
        return ResponseEntity.ok(ApiResponse.success(null, "Client deleted successfully"));
    }
} 