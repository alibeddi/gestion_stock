package com.polytech.gestionstock.controller;

import com.polytech.gestionstock.model.dto.ContactDto;
import com.polytech.gestionstock.model.response.ApiResponse;
import com.polytech.gestionstock.service.ContactService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contacts")
@RequiredArgsConstructor
@Slf4j
public class ContactController {

    private final ContactService contactService;

    @PostMapping
    public ResponseEntity<ApiResponse<ContactDto>> createContact(@Valid @RequestBody ContactDto contactDto) {
        log.info("Creating new contact: {}", contactDto.getNom());
        
        ContactDto savedContact = contactService.save(contactDto);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(savedContact, "Contact created successfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ContactDto>>> getAllContacts() {
        log.info("Fetching all contacts");
        
        List<ContactDto> contacts = contactService.findAll();
        
        return ResponseEntity.ok(ApiResponse.success(contacts, "Contacts retrieved successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ContactDto>> getContactById(@PathVariable Long id) {
        log.info("Fetching contact with ID: {}", id);
        
        ContactDto contact = contactService.findById(id);
        
        return ResponseEntity.ok(ApiResponse.success(contact, "Contact retrieved successfully"));
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<ApiResponse<List<ContactDto>>> getContactsByClient(@PathVariable Long clientId) {
        log.info("Fetching contacts for client ID: {}", clientId);
        
        List<ContactDto> contacts = contactService.findByClientId(clientId);
        
        return ResponseEntity.ok(ApiResponse.success(contacts, "Contacts retrieved successfully"));
    }

    @GetMapping("/nom/{nom}")
    public ResponseEntity<ApiResponse<List<ContactDto>>> getContactsByNom(@PathVariable String nom) {
        log.info("Fetching contacts with nom containing: {}", nom);
        
        List<ContactDto> contacts = contactService.findByNom(nom);
        
        return ResponseEntity.ok(ApiResponse.success(contacts, "Contacts retrieved successfully"));
    }

    @GetMapping("/prenom/{prenom}")
    public ResponseEntity<ApiResponse<List<ContactDto>>> getContactsByPrenom(@PathVariable String prenom) {
        log.info("Fetching contacts with prenom containing: {}", prenom);
        
        List<ContactDto> contacts = contactService.findByPrenom(prenom);
        
        return ResponseEntity.ok(ApiResponse.success(contacts, "Contacts retrieved successfully"));
    }

    @GetMapping("/fonction/{fonction}")
    public ResponseEntity<ApiResponse<List<ContactDto>>> getContactsByFonction(@PathVariable String fonction) {
        log.info("Fetching contacts with fonction containing: {}", fonction);
        
        List<ContactDto> contacts = contactService.findByFonction(fonction);
        
        return ResponseEntity.ok(ApiResponse.success(contacts, "Contacts retrieved successfully"));
    }

    @GetMapping("/societe/{nomSociete}")
    public ResponseEntity<ApiResponse<List<ContactDto>>> getContactsByNomSociete(@PathVariable String nomSociete) {
        log.info("Fetching contacts with nom de societe containing: {}", nomSociete);
        
        List<ContactDto> contacts = contactService.findByNomSociete(nomSociete);
        
        return ResponseEntity.ok(ApiResponse.success(contacts, "Contacts retrieved successfully"));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<ApiResponse<List<ContactDto>>> getContactsByEmail(@PathVariable String email) {
        log.info("Fetching contacts with email containing: {}", email);
        
        List<ContactDto> contacts = contactService.findByEmail(email);
        
        return ResponseEntity.ok(ApiResponse.success(contacts, "Contacts retrieved successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ContactDto>> updateContact(
            @PathVariable Long id, 
            @Valid @RequestBody ContactDto contactDto) {
        log.info("Updating contact with ID: {}", id);
        
        ContactDto updatedContact = contactService.update(id, contactDto);
        
        return ResponseEntity.ok(ApiResponse.success(updatedContact, "Contact updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteContact(@PathVariable Long id) {
        log.info("Deleting contact with ID: {}", id);
        
        contactService.delete(id);
        
        return ResponseEntity.ok(ApiResponse.success(null, "Contact deleted successfully"));
    }
} 