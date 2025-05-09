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
import org.springframework.web.bind.annotation.RestController;

import com.polytech.gestionstock.model.dto.ContactDto;
import com.polytech.gestionstock.model.response.ApiResponse;
import com.polytech.gestionstock.service.ContactService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(path = "/contacts")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Contact", description = "The Contact API")
public class ContactController {

    private final ContactService contactService;

    @Operation(summary = "Create a new contact", description = "Creates a new contact and returns the created contact")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Contact created successfully", 
                     content = @Content(schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PostMapping(path = "")
    public ResponseEntity<ApiResponse<ContactDto>> createContact(
            @Parameter(description = "Contact to create", required = true) 
            @Valid @RequestBody ContactDto contactDto) {
        log.info("Creating new contact: {}", contactDto.getNom());
        log.debug("Contact data received: {}", contactDto);
        
        ContactDto savedContact = contactService.save(contactDto);
        
        log.info("Contact created successfully with ID: {}", savedContact.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(savedContact, "Contact created successfully"));
    }

    @Operation(summary = "Get all contacts", description = "Returns a list of all contacts")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Contacts retrieved successfully", 
                     content = @Content(schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @GetMapping(path = "")
    public ResponseEntity<ApiResponse<List<ContactDto>>> getAllContacts() {
        log.info("Fetching all contacts");
        
        List<ContactDto> contacts = contactService.findAll();
        
        log.info("Retrieved {} contacts", contacts.size());
        return ResponseEntity.ok(ApiResponse.success(contacts, "Contacts retrieved successfully"));
    }

    @Operation(summary = "Get contact by ID", description = "Returns a contact by ID")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Contact retrieved successfully", 
                     content = @Content(schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Contact not found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @GetMapping(path = "/{id}")
    public ResponseEntity<ApiResponse<ContactDto>> getContactById(
            @Parameter(description = "ID of the contact to retrieve", required = true) 
            @PathVariable Long id) {
        log.info("Fetching contact with ID: {}", id);
        
        ContactDto contact = contactService.findById(id);
        
        log.info("Retrieved contact: {}", contact.getNom());
        return ResponseEntity.ok(ApiResponse.success(contact, "Contact retrieved successfully"));
    }

    @Operation(summary = "Get contacts by client ID", description = "Returns a list of contacts for a specific client")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Contacts retrieved successfully", 
                     content = @Content(schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @GetMapping("/client/{clientId}")
    public ResponseEntity<ApiResponse<List<ContactDto>>> getContactsByClient(
            @Parameter(description = "ID of the client", required = true) 
            @PathVariable Long clientId) {
        log.info("Fetching contacts for client ID: {}", clientId);
        
        List<ContactDto> contacts = contactService.findByClientId(clientId);
        
        return ResponseEntity.ok(ApiResponse.success(contacts, "Contacts retrieved successfully"));
    }

    @Operation(summary = "Get contacts by name", description = "Returns a list of contacts whose names contain the given string")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Contacts retrieved successfully", 
                     content = @Content(schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @GetMapping("/nom/{nom}")
    public ResponseEntity<ApiResponse<List<ContactDto>>> getContactsByNom(
            @Parameter(description = "Name to search for", required = true) 
            @PathVariable String nom) {
        log.info("Fetching contacts with nom containing: {}", nom);
        
        List<ContactDto> contacts = contactService.findByNom(nom);
        
        return ResponseEntity.ok(ApiResponse.success(contacts, "Contacts retrieved successfully"));
    }

    @Operation(summary = "Get contacts by first name", description = "Returns a list of contacts whose first names contain the given string")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Contacts retrieved successfully", 
                     content = @Content(schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @GetMapping("/prenom/{prenom}")
    public ResponseEntity<ApiResponse<List<ContactDto>>> getContactsByPrenom(
            @Parameter(description = "First name to search for", required = true) 
            @PathVariable String prenom) {
        log.info("Fetching contacts with prenom containing: {}", prenom);
        
        List<ContactDto> contacts = contactService.findByPrenom(prenom);
        
        return ResponseEntity.ok(ApiResponse.success(contacts, "Contacts retrieved successfully"));
    }

    @Operation(summary = "Get contacts by function", description = "Returns a list of contacts whose function contains the given string")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Contacts retrieved successfully", 
                     content = @Content(schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @GetMapping("/fonction/{fonction}")
    public ResponseEntity<ApiResponse<List<ContactDto>>> getContactsByFonction(
            @Parameter(description = "Function to search for", required = true) 
            @PathVariable String fonction) {
        log.info("Fetching contacts with fonction containing: {}", fonction);
        
        List<ContactDto> contacts = contactService.findByFonction(fonction);
        
        return ResponseEntity.ok(ApiResponse.success(contacts, "Contacts retrieved successfully"));
    }

    @Operation(summary = "Get contacts by company name", description = "Returns a list of contacts by company name")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Contacts retrieved successfully", 
                     content = @Content(schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @GetMapping("/societe/{nomSociete}")
    public ResponseEntity<ApiResponse<List<ContactDto>>> getContactsByNomSociete(
            @Parameter(description = "Company name to search for", required = true) 
            @PathVariable String nomSociete) {
        log.info("Fetching contacts with nom de societe containing: {}", nomSociete);
        
        List<ContactDto> contacts = contactService.findByNomSociete(nomSociete);
        
        return ResponseEntity.ok(ApiResponse.success(contacts, "Contacts retrieved successfully"));
    }

    @Operation(summary = "Get contacts by email", description = "Returns a list of contacts by email")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Contacts retrieved successfully", 
                     content = @Content(schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @GetMapping("/email/{email}")
    public ResponseEntity<ApiResponse<List<ContactDto>>> getContactsByEmail(
            @Parameter(description = "Email to search for", required = true) 
            @PathVariable String email) {
        log.info("Fetching contacts with email containing: {}", email);
        
        List<ContactDto> contacts = contactService.findByEmail(email);
        
        return ResponseEntity.ok(ApiResponse.success(contacts, "Contacts retrieved successfully"));
    }

    @Operation(summary = "Update a contact", description = "Updates a contact by ID")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Contact updated successfully", 
                     content = @Content(schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Contact not found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PutMapping(path = "/{id}")
    public ResponseEntity<ApiResponse<ContactDto>> updateContact(
            @Parameter(description = "ID of the contact to update", required = true) 
            @PathVariable Long id, 
            @Parameter(description = "Updated contact data", required = true) 
            @Valid @RequestBody ContactDto contactDto) {
        log.info("Updating contact with ID: {}", id);
        log.debug("Contact update data: {}", contactDto);
        
        ContactDto updatedContact = contactService.update(id, contactDto);
        
        log.info("Contact updated successfully");
        return ResponseEntity.ok(ApiResponse.success(updatedContact, "Contact updated successfully"));
    }

    @Operation(summary = "Delete a contact", description = "Deletes a contact by ID")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Contact deleted successfully", 
                     content = @Content(schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Contact not found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteContact(
            @Parameter(description = "ID of the contact to delete", required = true) 
            @PathVariable Long id) {
        log.info("Deleting contact with ID: {}", id);
        
        contactService.delete(id);
        
        log.info("Contact deleted successfully");
        return ResponseEntity.ok(ApiResponse.success(null, "Contact deleted successfully"));
    }
} 