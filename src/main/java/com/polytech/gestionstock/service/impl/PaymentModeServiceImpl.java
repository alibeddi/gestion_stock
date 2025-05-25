package com.polytech.gestionstock.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.polytech.gestionstock.exception.DuplicateEntityException;
import com.polytech.gestionstock.exception.EntityNotFoundException;
import com.polytech.gestionstock.model.dto.PaymentModeDto;
import com.polytech.gestionstock.model.entity.PaymentMode;
import com.polytech.gestionstock.model.entity.User;
import com.polytech.gestionstock.repository.PaymentModeRepository;
import com.polytech.gestionstock.repository.UserRepository;
import com.polytech.gestionstock.service.PaymentModeService;
import com.polytech.gestionstock.util.ObjectMapperUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentModeServiceImpl implements PaymentModeService {

    private final PaymentModeRepository paymentModeRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public PaymentModeDto save(PaymentModeDto paymentModeDto, String creatorEmail) {
        log.info("Saving payment mode with name: {}", paymentModeDto.getName());
        
        if (paymentModeRepository.existsByName(paymentModeDto.getName())) {
            throw new DuplicateEntityException("PaymentMode", "name", paymentModeDto.getName());
        }
        
        if (paymentModeRepository.existsByCode(paymentModeDto.getCode())) {
            throw new DuplicateEntityException("PaymentMode", "code", paymentModeDto.getCode());
        }
        
        // Get the admin user who is creating this payment mode
        User creator = userRepository.findByEmail(creatorEmail)
                .orElseThrow(() -> new EntityNotFoundException("User", "email", creatorEmail));
        
        PaymentMode paymentMode = ObjectMapperUtils.mapToEntity(paymentModeDto, PaymentMode.class);
        paymentMode.setCreatedBy(creator);
        paymentMode.setActive(true);
        
        paymentMode = paymentModeRepository.save(paymentMode);
        return ObjectMapperUtils.mapToDto(paymentMode, PaymentModeDto.class);
    }

    @Override
    @Transactional
    public PaymentModeDto update(Long id, PaymentModeDto paymentModeDto) {
        log.info("Updating payment mode with ID: {}", id);
        
        PaymentMode existingPaymentMode = paymentModeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("PaymentMode", "id", id));
        
        // Check if name is being changed and if it's already taken
        if (!existingPaymentMode.getName().equals(paymentModeDto.getName()) && 
                paymentModeRepository.existsByName(paymentModeDto.getName())) {
            throw new DuplicateEntityException("PaymentMode", "name", paymentModeDto.getName());
        }
        
        // Check if code is being changed and if it's already taken
        if (!existingPaymentMode.getCode().equals(paymentModeDto.getCode()) && 
                paymentModeRepository.existsByCode(paymentModeDto.getCode())) {
            throw new DuplicateEntityException("PaymentMode", "code", paymentModeDto.getCode());
        }
        
        // Update fields but preserve id and createdBy
        User createdBy = existingPaymentMode.getCreatedBy();
        
        existingPaymentMode.setName(paymentModeDto.getName());
        existingPaymentMode.setDescription(paymentModeDto.getDescription());
        existingPaymentMode.setCode(paymentModeDto.getCode());
        
        // Do not update active status here - use activate/deactivate methods instead
        
        existingPaymentMode = paymentModeRepository.save(existingPaymentMode);
        return ObjectMapperUtils.mapToDto(existingPaymentMode, PaymentModeDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentModeDto findById(Long id) {
        log.info("Finding payment mode by ID: {}", id);
        
        return paymentModeRepository.findById(id)
                .map(paymentMode -> ObjectMapperUtils.mapToDto(paymentMode, PaymentModeDto.class))
                .orElseThrow(() -> new EntityNotFoundException("PaymentMode", "id", id));
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentModeDto findByCode(String code) {
        log.info("Finding payment mode by code: {}", code);
        
        return paymentModeRepository.findByCode(code)
                .map(paymentMode -> ObjectMapperUtils.mapToDto(paymentMode, PaymentModeDto.class))
                .orElseThrow(() -> new EntityNotFoundException("PaymentMode", "code", code));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentModeDto> findAll() {
        log.info("Finding all payment modes");
        
        List<PaymentMode> paymentModes = paymentModeRepository.findAll();
        return ObjectMapperUtils.mapAll(paymentModes, PaymentModeDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentModeDto> findAllActive() {
        log.info("Finding all active payment modes");
        
        List<PaymentMode> paymentModes = paymentModeRepository.findByActive(true);
        return ObjectMapperUtils.mapAll(paymentModes, PaymentModeDto.class);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Deleting payment mode with ID: {}", id);
        
        if (!paymentModeRepository.existsById(id)) {
            throw new EntityNotFoundException("PaymentMode", "id", id);
        }
        
        paymentModeRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void activate(Long id) {
        log.info("Activating payment mode with ID: {}", id);
        
        PaymentMode paymentMode = paymentModeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("PaymentMode", "id", id));
        
        paymentMode.setActive(true);
        paymentModeRepository.save(paymentMode);
    }

    @Override
    @Transactional
    public void deactivate(Long id) {
        log.info("Deactivating payment mode with ID: {}", id);
        
        PaymentMode paymentMode = paymentModeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("PaymentMode", "id", id));
        
        paymentMode.setActive(false);
        paymentModeRepository.save(paymentMode);
    }
} 