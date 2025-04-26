package com.polytech.gestionstock.service;

import com.polytech.gestionstock.model.dto.ContactDto;

import java.util.List;

public interface ContactService {
    ContactDto save(ContactDto contactDto);
    ContactDto update(Long id, ContactDto contactDto);
    ContactDto findById(Long id);
    List<ContactDto> findByClientId(Long clientId);
    List<ContactDto> findByNom(String nom);
    List<ContactDto> findByPrenom(String prenom);
    List<ContactDto> findByFonction(String fonction);
    List<ContactDto> findByNomSociete(String nomSociete);
    List<ContactDto> findByEmail(String email);
    List<ContactDto> findAll();
    void delete(Long id);
} 