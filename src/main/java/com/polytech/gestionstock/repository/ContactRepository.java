package com.polytech.gestionstock.repository;

import com.polytech.gestionstock.model.entity.Client;
import com.polytech.gestionstock.model.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    List<Contact> findByClient(Client client);
    List<Contact> findByNomContainingIgnoreCase(String nom);
    List<Contact> findByPrenomContainingIgnoreCase(String prenom);
    List<Contact> findByFonctionContainingIgnoreCase(String fonction);
    List<Contact> findByNomSocieteContainingIgnoreCase(String nomSociete);
    List<Contact> findByEmailContainingIgnoreCase(String email);
} 