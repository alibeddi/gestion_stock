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

import com.polytech.gestionstock.model.dto.ProduitDto;
import com.polytech.gestionstock.model.response.ApiResponse;
import com.polytech.gestionstock.service.ProduitService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/produits")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Products", description = "Product management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class ProduitController {

    private final ProduitService produitService;

    @Operation(
        summary = "Create a new product",
        description = "Creates a new product with the provided details. Requires ADMIN or MANAGER role."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Product created successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request body or data validation error"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - Not authenticated"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden - Not authorized to create products"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Conflict - Product with the same code already exists")
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<ProduitDto>> createProduit(
            @Parameter(description = "Product data to create", required = true)
            @Valid @RequestBody ProduitDto produitDto) {
        
        log.info("Creating new produit: {}", produitDto.getCode());
        
        ProduitDto savedProduit = produitService.save(produitDto);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(savedProduit, "Produit created successfully"));
    }

    @Operation(
        summary = "Get all products",
        description = "Retrieves a list of all products in the system."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Products retrieved successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - Not authenticated")
    })
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProduitDto>>> getAllProduits() {
        log.info("Fetching all produits");
        
        List<ProduitDto> produits = produitService.findAll();
        
        return ResponseEntity.ok(ApiResponse.success(produits, "Produits retrieved successfully"));
    }

    @Operation(
        summary = "Get all active products",
        description = "Retrieves a list of all active products in the system."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Active products retrieved successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - Not authenticated")
    })
    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<ProduitDto>>> getAllActiveProduits() {
        log.info("Fetching all active produits");
        
        List<ProduitDto> produits = produitService.findAllActiveProducts();
        
        return ResponseEntity.ok(ApiResponse.success(produits, "Active produits retrieved successfully"));
    }

    @Operation(
        summary = "Get product by ID",
        description = "Retrieves a specific product by its unique identifier."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Product retrieved successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - Not authenticated"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProduitDto>> getProduitById(
            @Parameter(description = "Product ID", required = true)
            @PathVariable Long id) {
        
        log.info("Fetching produit with ID: {}", id);
        
        ProduitDto produit = produitService.findById(id);
        
        return ResponseEntity.ok(ApiResponse.success(produit, "Produit retrieved successfully"));
    }

    @Operation(
        summary = "Get product by code",
        description = "Retrieves a specific product by its unique code."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Product retrieved successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - Not authenticated"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping("/code/{code}")
    public ResponseEntity<ApiResponse<ProduitDto>> getProduitByCode(
            @Parameter(description = "Product code", required = true)
            @PathVariable String code) {
        
        log.info("Fetching produit with code: {}", code);
        
        ProduitDto produit = produitService.findByCode(code);
        
        return ResponseEntity.ok(ApiResponse.success(produit, "Produit retrieved successfully"));
    }

    @Operation(
        summary = "Get products by libelle",
        description = "Retrieves products with a libelle (name) containing the specified text (case-insensitive)."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Products retrieved successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - Not authenticated")
    })
    @GetMapping("/libelle/{libelle}")
    public ResponseEntity<ApiResponse<List<ProduitDto>>> getProduitsByLibelle(
            @Parameter(description = "Text to search in product names", required = true)
            @PathVariable String libelle) {
        
        log.info("Fetching produits with libelle containing: {}", libelle);
        
        List<ProduitDto> produits = produitService.findByLibelle(libelle);
        
        return ResponseEntity.ok(ApiResponse.success(produits, "Produits retrieved successfully"));
    }

    @Operation(
        summary = "Get products by packaging/emballage",
        description = "Retrieves all products associated with a specific packaging/emballage type."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Products retrieved successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - Not authenticated"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Emballage not found")
    })
    @GetMapping("/emballage/{emballageId}")
    public ResponseEntity<ApiResponse<List<ProduitDto>>> getProduitsByEmballage(
            @Parameter(description = "Emballage ID", required = true)
            @PathVariable Long emballageId) {
        
        log.info("Fetching produits by emballage ID: {}", emballageId);
        
        List<ProduitDto> produits = produitService.findByEmballageId(emballageId);
        
        return ResponseEntity.ok(ApiResponse.success(produits, "Produits retrieved successfully"));
    }

    @Operation(
        summary = "Get products by category",
        description = "Retrieves all products belonging to a specific category."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Products retrieved successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - Not authenticated")
    })
    @GetMapping("/categorie/{categorie}")
    public ResponseEntity<ApiResponse<List<ProduitDto>>> getProduitsByCategorie(
            @Parameter(description = "Category name", required = true)
            @PathVariable String categorie) {
        
        log.info("Fetching produits by categorie: {}", categorie);
        
        List<ProduitDto> produits = produitService.findByCategorie(categorie);
        
        return ResponseEntity.ok(ApiResponse.success(produits, "Produits retrieved successfully"));
    }

    @Operation(
        summary = "Get products by type",
        description = "Retrieves all products of a specific type."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Products retrieved successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - Not authenticated")
    })
    @GetMapping("/type/{typeProduit}")
    public ResponseEntity<ApiResponse<List<ProduitDto>>> getProduitsByType(
            @Parameter(description = "Product type", required = true)
            @PathVariable String typeProduit) {
        
        log.info("Fetching produits by type: {}", typeProduit);
        
        List<ProduitDto> produits = produitService.findByTypeProduit(typeProduit);
        
        return ResponseEntity.ok(ApiResponse.success(produits, "Produits retrieved successfully"));
    }

    @Operation(
        summary = "Get products by active status",
        description = "Retrieves all products with the specified active status."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Products retrieved successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - Not authenticated")
    })
    @GetMapping("/actif/{actif}")
    public ResponseEntity<ApiResponse<List<ProduitDto>>> getProduitsByActif(
            @Parameter(description = "Active status (true/false)", required = true)
            @PathVariable Boolean actif) {
        
        log.info("Fetching produits by actif: {}", actif);
        
        List<ProduitDto> produits = produitService.findByActif(actif);
        
        return ResponseEntity.ok(ApiResponse.success(produits, "Produits retrieved successfully"));
    }

    @Operation(
        summary = "Get products by package status",
        description = "Retrieves all products with the specified package status."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Products retrieved successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - Not authenticated")
    })
    @GetMapping("/package/{isPackage}")
    public ResponseEntity<ApiResponse<List<ProduitDto>>> getProduitsByIsPackage(
            @Parameter(description = "Package status (true/false)", required = true)
            @PathVariable Boolean isPackage) {
        
        log.info("Fetching produits by isPackage: {}", isPackage);
        
        List<ProduitDto> produits = produitService.findByIsPackage(isPackage);
        
        return ResponseEntity.ok(ApiResponse.success(produits, "Produits retrieved successfully"));
    }

    @Operation(
        summary = "Get products by ecozit status",
        description = "Retrieves all products with the specified ecozit status."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Products retrieved successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - Not authenticated")
    })
    @GetMapping("/ecozit/{ecozit}")
    public ResponseEntity<ApiResponse<List<ProduitDto>>> getProduitsByEcozit(
            @Parameter(description = "Ecozit status (true/false)", required = true)
            @PathVariable Boolean ecozit) {
        
        log.info("Fetching produits by ecozit: {}", ecozit);
        
        List<ProduitDto> produits = produitService.findByEcozit(ecozit);
        
        return ResponseEntity.ok(ApiResponse.success(produits, "Produits retrieved successfully"));
    }

    @Operation(
        summary = "Update a product",
        description = "Updates an existing product with the provided details. Requires ADMIN or MANAGER role."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Product updated successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request body or data validation error"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - Not authenticated"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden - Not authorized to update products"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Product not found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Conflict - Another product with the same code already exists")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<ProduitDto>> updateProduit(
            @Parameter(description = "Product ID to update", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated product data", required = true)
            @Valid @RequestBody ProduitDto produitDto) {
        
        log.info("Updating produit with ID: {}", id);
        
        ProduitDto updatedProduit = produitService.update(id, produitDto);
        
        return ResponseEntity.ok(ApiResponse.success(updatedProduit, "Produit updated successfully"));
    }

    @Operation(
        summary = "Delete a product",
        description = "Deletes a product by its ID. Requires ADMIN or MANAGER role."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Product deleted successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - Not authenticated"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden - Not authorized to delete products"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Product not found")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Void>> deleteProduit(
            @Parameter(description = "Product ID to delete", required = true)
            @PathVariable Long id) {
        
        log.info("Deleting produit with ID: {}", id);
        
        produitService.delete(id);
        
        return ResponseEntity.ok(ApiResponse.success(null, "Produit deleted successfully"));
    }
} 