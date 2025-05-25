package com.polytech.gestionstock.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentModeDto {
    private Long id;
    
    @NotBlank(message = "Payment mode name is required")
    @Size(max = 100, message = "Payment mode name must be less than 100 characters")
    private String name;
    
    @Size(max = 255, message = "Description must be less than 255 characters")
    private String description;
    
    @NotBlank(message = "Payment mode code is required")
    @Size(max = 20, message = "Code must be less than 20 characters")
    private String code;
    
    private boolean active;
    
    private UserDto createdBy;
} 