package com.polytech.gestionstock.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.polytech.gestionstock.model.dto.PaymentModeDto;
import com.polytech.gestionstock.model.response.ApiResponse;
import com.polytech.gestionstock.service.PaymentModeService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/payment-modes")
@RequiredArgsConstructor
@Slf4j
public class PaymentModeController {

    private final PaymentModeService paymentModeService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<PaymentModeDto>>> getAllPaymentModes() {
        log.info("Fetching all payment modes");
        
        List<PaymentModeDto> paymentModes = paymentModeService.findAll();
        
        return ResponseEntity.ok(ApiResponse.success(paymentModes, "Payment modes retrieved successfully"));
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<PaymentModeDto>>> getAllActivePaymentModes() {
        log.info("Fetching all active payment modes");
        
        List<PaymentModeDto> paymentModes = paymentModeService.findAllActive();
        
        return ResponseEntity.ok(ApiResponse.success(paymentModes, "Active payment modes retrieved successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PaymentModeDto>> getPaymentModeById(@PathVariable Long id) {
        log.info("Fetching payment mode with ID: {}", id);
        
        PaymentModeDto paymentMode = paymentModeService.findById(id);
        
        return ResponseEntity.ok(ApiResponse.success(paymentMode, "Payment mode retrieved successfully"));
    }
    
    @GetMapping("/code/{code}")
    public ResponseEntity<ApiResponse<PaymentModeDto>> getPaymentModeByCode(@PathVariable String code) {
        log.info("Fetching payment mode with code: {}", code);
        
        PaymentModeDto paymentMode = paymentModeService.findByCode(code);
        
        return ResponseEntity.ok(ApiResponse.success(paymentMode, "Payment mode retrieved successfully"));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PaymentModeDto>> createPaymentMode(@Valid @RequestBody PaymentModeDto paymentModeDto) {
        log.info("Creating new payment mode with name: {}", paymentModeDto.getName());
        
        // Get the current admin user who is creating this payment mode
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();
        
        PaymentModeDto createdPaymentMode = paymentModeService.save(paymentModeDto, currentUserEmail);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(createdPaymentMode, "Payment mode created successfully"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PaymentModeDto>> updatePaymentMode(
            @PathVariable Long id, 
            @Valid @RequestBody PaymentModeDto paymentModeDto) {
        log.info("Updating payment mode with ID: {}", id);
        
        PaymentModeDto updatedPaymentMode = paymentModeService.update(id, paymentModeDto);
        
        return ResponseEntity.ok(ApiResponse.success(updatedPaymentMode, "Payment mode updated successfully"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deletePaymentMode(@PathVariable Long id) {
        log.info("Deleting payment mode with ID: {}", id);
        
        paymentModeService.delete(id);
        
        return ResponseEntity.ok(ApiResponse.success(null, "Payment mode deleted successfully"));
    }
    
    @PutMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> activatePaymentMode(@PathVariable Long id) {
        log.info("Activating payment mode with ID: {}", id);
        
        paymentModeService.activate(id);
        
        return ResponseEntity.ok(ApiResponse.success(null, "Payment mode activated successfully"));
    }
    
    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deactivatePaymentMode(@PathVariable Long id) {
        log.info("Deactivating payment mode with ID: {}", id);
        
        paymentModeService.deactivate(id);
        
        return ResponseEntity.ok(ApiResponse.success(null, "Payment mode deactivated successfully"));
    }
} 