package com.polytech.gestionstock.service;

import com.polytech.gestionstock.model.dto.SecteurActiviteDto;

import java.util.List;

public interface SecteurActiviteService {
    SecteurActiviteDto save(SecteurActiviteDto secteurActiviteDto);
    SecteurActiviteDto update(Long id, SecteurActiviteDto secteurActiviteDto);
    SecteurActiviteDto findById(Long id);
    SecteurActiviteDto findByCode(String code);
    List<SecteurActiviteDto> findAll();
    void delete(Long id);
} 