package com.polytech.gestionstock.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.polytech.gestionstock.exception.EntityNotFoundException;
import com.polytech.gestionstock.model.dto.ContactDto;
import com.polytech.gestionstock.model.entity.Client;
import com.polytech.gestionstock.model.entity.Contact;
import com.polytech.gestionstock.repository.ClientRepository;
import com.polytech.gestionstock.repository.ContactRepository;
import com.polytech.gestionstock.service.ContactService;
import com.polytech.gestionstock.util.ObjectMapperUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;
    private final ClientRepository clientRepository;

    @Override
    @Transactional
    public ContactDto save(ContactDto contactDto) {
        log.info("Saving contact with name: {}", contactDto.getNom());
        
        Contact contact = ObjectMapperUtils.mapToEntity(contactDto, Contact.class);
        
        // Set client if provided
        if (contactDto.getClientId() != null) {
            Client client = clientRepository.findById(contactDto.getClientId())
                    .orElseThrow(() -> new EntityNotFoundException("Client", "id", contactDto.getClientId()));
            contact.setClient(client);
        }
        
        contact = contactRepository.save(contact);
        return ObjectMapperUtils.mapToDto(contact, ContactDto.class);
    }

    @Override
    @Transactional
    public ContactDto update(Long id, ContactDto contactDto) {
        log.info("Updating contact with ID: {}", id);
        
        Contact existingContact = contactRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contact", "id", id));
        
        // Update fields but preserve ID
        ObjectMapperUtils.updateEntityFromDto(contactDto, existingContact);
        existingContact.setId(id);
        
        // Set client if provided
        if (contactDto.getClientId() != null) {
            Client client = clientRepository.findById(contactDto.getClientId())
                    .orElseThrow(() -> new EntityNotFoundException("Client", "id", contactDto.getClientId()));
            existingContact.setClient(client);
        }
        
        existingContact = contactRepository.save(existingContact);
        return ObjectMapperUtils.mapToDto(existingContact, ContactDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public ContactDto findById(Long id) {
        log.info("Finding contact by ID: {}", id);
        
        return contactRepository.findById(id)
                .map(contact -> {
                    ContactDto contactDto = ObjectMapperUtils.mapToDto(contact, ContactDto.class);
                    if (contact.getClient() != null) {
                        contactDto.setClientId(contact.getClient().getId());
                    }
                    return contactDto;
                })
                .orElseThrow(() -> new EntityNotFoundException("Contact", "id", id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContactDto> findByClientId(Long clientId) {
        log.info("Finding contacts by client ID: {}", clientId);
        
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new EntityNotFoundException("Client", "id", clientId));
        
        List<Contact> contacts = contactRepository.findByClient(client);
        List<ContactDto> contactDtos = ObjectMapperUtils.mapAll(contacts, ContactDto.class);
        
        // Set client ID for each DTO
        contactDtos.forEach(dto -> dto.setClientId(clientId));
        
        return contactDtos;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContactDto> findByNom(String nom) {
        log.info("Finding contacts by nom containing: {}", nom);
        
        List<Contact> contacts = contactRepository.findByNomContainingIgnoreCase(nom);
        List<ContactDto> contactDtos = ObjectMapperUtils.mapAll(contacts, ContactDto.class);
        
        // Set client ID for each DTO
        contactDtos.forEach(dto -> {
            Contact contact = contacts.stream()
                    .filter(c -> c.getId().equals(dto.getId()))
                    .findFirst()
                    .orElse(null);
            
            if (contact != null && contact.getClient() != null) {
                dto.setClientId(contact.getClient().getId());
            }
        });
        
        return contactDtos;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContactDto> findByPrenom(String prenom) {
        log.info("Finding contacts by prénom containing: {}", prenom);
        
        List<Contact> contacts = contactRepository.findByPrenomContainingIgnoreCase(prenom);
        List<ContactDto> contactDtos = ObjectMapperUtils.mapAll(contacts, ContactDto.class);
        
        // Set client ID for each DTO
        contactDtos.forEach(dto -> {
            Contact contact = contacts.stream()
                    .filter(c -> c.getId().equals(dto.getId()))
                    .findFirst()
                    .orElse(null);
            
            if (contact != null && contact.getClient() != null) {
                dto.setClientId(contact.getClient().getId());
            }
        });
        
        return contactDtos;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContactDto> findByFonction(String fonction) {
        log.info("Finding contacts by fonction containing: {}", fonction);
        
        List<Contact> contacts = contactRepository.findByFonctionContainingIgnoreCase(fonction);
        List<ContactDto> contactDtos = ObjectMapperUtils.mapAll(contacts, ContactDto.class);
        
        // Set client ID for each DTO
        contactDtos.forEach(dto -> {
            Contact contact = contacts.stream()
                    .filter(c -> c.getId().equals(dto.getId()))
                    .findFirst()
                    .orElse(null);
            
            if (contact != null && contact.getClient() != null) {
                dto.setClientId(contact.getClient().getId());
            }
        });
        
        return contactDtos;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContactDto> findByNomSociete(String nomSociete) {
        log.info("Finding contacts by nom société containing: {}", nomSociete);
        
        List<Contact> contacts = contactRepository.findByNomSocieteContainingIgnoreCase(nomSociete);
        List<ContactDto> contactDtos = ObjectMapperUtils.mapAll(contacts, ContactDto.class);
        
        // Set client ID for each DTO
        contactDtos.forEach(dto -> {
            Contact contact = contacts.stream()
                    .filter(c -> c.getId().equals(dto.getId()))
                    .findFirst()
                    .orElse(null);
            
            if (contact != null && contact.getClient() != null) {
                dto.setClientId(contact.getClient().getId());
            }
        });
        
        return contactDtos;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContactDto> findByEmail(String email) {
        log.info("Finding contacts by email containing: {}", email);
        
        List<Contact> contacts = contactRepository.findByEmailContainingIgnoreCase(email);
        List<ContactDto> contactDtos = ObjectMapperUtils.mapAll(contacts, ContactDto.class);
        
        // Set client ID for each DTO
        contactDtos.forEach(dto -> {
            Contact contact = contacts.stream()
                    .filter(c -> c.getId().equals(dto.getId()))
                    .findFirst()
                    .orElse(null);
            
            if (contact != null && contact.getClient() != null) {
                dto.setClientId(contact.getClient().getId());
            }
        });
        
        return contactDtos;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContactDto> findAll() {
        log.info("Finding all contacts");
        
        List<Contact> contacts = contactRepository.findAll();
        List<ContactDto> contactDtos = ObjectMapperUtils.mapAll(contacts, ContactDto.class);
        
        // Set client ID for each DTO
        contactDtos.forEach(dto -> {
            Contact contact = contacts.stream()
                    .filter(c -> c.getId().equals(dto.getId()))
                    .findFirst()
                    .orElse(null);
            
            if (contact != null && contact.getClient() != null) {
                dto.setClientId(contact.getClient().getId());
            }
        });
        
        return contactDtos;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Deleting contact with ID: {}", id);
        
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contact", "id", id));
        
        contactRepository.delete(contact);
    }
} 