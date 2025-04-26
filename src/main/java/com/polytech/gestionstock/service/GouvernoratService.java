package com.polytech.gestionstock.service;

import com.polytech.gestionstock.model.dto.GouvernoratDto;

import java.util.List;

public interface GouvernoratService {
    GouvernoratDto save(GouvernoratDto gouvernoratDto);
    GouvernoratDto update(Long id, GouvernoratDto gouvernoratDto);
    GouvernoratDto findById(Long id);
    GouvernoratDto findByCode(String code);
    List<GouvernoratDto> findByPays(String pays);
    List<GouvernoratDto> findAll();
    void delete(Long id);
} 