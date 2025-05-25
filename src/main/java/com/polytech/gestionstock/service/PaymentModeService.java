package com.polytech.gestionstock.service;

import java.util.List;

import com.polytech.gestionstock.model.dto.PaymentModeDto;

public interface PaymentModeService {
    PaymentModeDto save(PaymentModeDto paymentModeDto, String creatorEmail);
    PaymentModeDto update(Long id, PaymentModeDto paymentModeDto);
    PaymentModeDto findById(Long id);
    PaymentModeDto findByCode(String code);
    List<PaymentModeDto> findAll();
    List<PaymentModeDto> findAllActive();
    void delete(Long id);
    void activate(Long id);
    void deactivate(Long id);
} 