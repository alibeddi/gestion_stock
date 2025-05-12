package com.polytech.gestionstock.model.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSettingsDto {
    private Long id;
    private Long userId;
    private String language;
    private Boolean notificationEmail;
    private Boolean notificationSms;
    private LocalDateTime dateCreation;
    private LocalDateTime dateModification;
} 