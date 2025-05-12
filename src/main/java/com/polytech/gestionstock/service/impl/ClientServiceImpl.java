package com.polytech.gestionstock.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.polytech.gestionstock.exception.DuplicateEntityException;
import com.polytech.gestionstock.exception.EntityNotFoundException;
import com.polytech.gestionstock.model.dto.ClientDto;
import com.polytech.gestionstock.model.dto.ProspectDto;
import com.polytech.gestionstock.model.entity.Client;
import com.polytech.gestionstock.model.entity.Gouvernorat;
import com.polytech.gestionstock.model.entity.SecteurActivite;
import com.polytech.gestionstock.repository.ClientRepository;
import com.polytech.gestionstock.repository.GouvernoratRepository;
import com.polytech.gestionstock.repository.SecteurActiviteRepository;
import com.polytech.gestionstock.service.ClientService;
import com.polytech.gestionstock.util.IdGenerator;
import com.polytech.gestionstock.util.ObjectMapperUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final SecteurActiviteRepository secteurActiviteRepository;
    private final GouvernoratRepository gouvernoratRepository;

    @Override
    @Transactional
    public ClientDto save(ClientDto clientDto) {
        log.info("Saving client with name: {}", clientDto.getNom());
        
        // Generate account number if not provided
        if (clientDto.getNumeroCompte() == null || clientDto.getNumeroCompte().isEmpty()) {
            clientDto.setNumeroCompte(IdGenerator.generateClientAccountNumber());
        } else if (clientRepository.findByNumeroCompte(clientDto.getNumeroCompte()).isPresent()) {
            throw new DuplicateEntityException("Client", "numeroCompte", clientDto.getNumeroCompte());
        }
        
        // Check matricule fiscal if provided
        if (clientDto.getMatriculeFiscal() != null && !clientDto.getMatriculeFiscal().isEmpty() 
                && clientRepository.findByMatriculeFiscal(clientDto.getMatriculeFiscal()).isPresent()) {
            throw new DuplicateEntityException("Client", "matriculeFiscal", clientDto.getMatriculeFiscal());
        }
        
        Client client = ObjectMapperUtils.mapToEntity(clientDto, Client.class);
        
        // Set references
        if (clientDto.getSecteurActivite() != null && clientDto.getSecteurActivite().getId() != null) {
            SecteurActivite secteurActivite = secteurActiviteRepository.findById(clientDto.getSecteurActivite().getId())
                    .orElseThrow(() -> new EntityNotFoundException("SecteurActivite", "id", clientDto.getSecteurActivite().getId()));
            client.setSecteurActivite(secteurActivite);
        }
        
        if (clientDto.getGouvernorat() != null && clientDto.getGouvernorat().getId() != null) {
            Gouvernorat gouvernorat = gouvernoratRepository.findById(clientDto.getGouvernorat().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Gouvernorat", "id", clientDto.getGouvernorat().getId()));
            client.setGouvernorat(gouvernorat);
        }
        
        client = clientRepository.save(client);
        return ObjectMapperUtils.mapToDto(client, ClientDto.class);
    }

    @Override
    @Transactional
    public ClientDto update(Long id, ClientDto clientDto) {
        log.info("Updating client with ID: {}", id);
        
        Client existingClient = clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Client", "id", id));
        
        // Check numero compte if changed
        if (clientDto.getNumeroCompte() != null && !clientDto.getNumeroCompte().equals(existingClient.getNumeroCompte()) &&
                clientRepository.findByNumeroCompte(clientDto.getNumeroCompte()).isPresent()) {
            throw new DuplicateEntityException("Client", "numeroCompte", clientDto.getNumeroCompte());
        }
        
        // Check matricule fiscal if changed
        if (clientDto.getMatriculeFiscal() != null && !clientDto.getMatriculeFiscal().equals(existingClient.getMatriculeFiscal()) &&
                clientRepository.findByMatriculeFiscal(clientDto.getMatriculeFiscal()).isPresent()) {
            throw new DuplicateEntityException("Client", "matriculeFiscal", clientDto.getMatriculeFiscal());
        }
        
        // Update fields but preserve ID
        ObjectMapperUtils.updateEntityFromDto(clientDto, existingClient);
        existingClient.setId(id);
        
        // Set references
        if (clientDto.getSecteurActivite() != null && clientDto.getSecteurActivite().getId() != null) {
            SecteurActivite secteurActivite = secteurActiviteRepository.findById(clientDto.getSecteurActivite().getId())
                    .orElseThrow(() -> new EntityNotFoundException("SecteurActivite", "id", clientDto.getSecteurActivite().getId()));
            existingClient.setSecteurActivite(secteurActivite);
        }
        
        if (clientDto.getGouvernorat() != null && clientDto.getGouvernorat().getId() != null) {
            Gouvernorat gouvernorat = gouvernoratRepository.findById(clientDto.getGouvernorat().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Gouvernorat", "id", clientDto.getGouvernorat().getId()));
            existingClient.setGouvernorat(gouvernorat);
        }
        
        existingClient = clientRepository.save(existingClient);
        return ObjectMapperUtils.mapToDto(existingClient, ClientDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public ClientDto findById(Long id) {
        log.info("Finding client by ID: {}", id);
        
        return clientRepository.findById(id)
                .map(client -> ObjectMapperUtils.mapToDto(client, ClientDto.class))
                .orElseThrow(() -> new EntityNotFoundException("Client", "id", id));
    }

    @Override
    @Transactional(readOnly = true)
    public ClientDto findByNumeroCompte(String numeroCompte) {
        log.info("Finding client by numéro compte: {}", numeroCompte);
        
        return clientRepository.findByNumeroCompte(numeroCompte)
                .map(client -> ObjectMapperUtils.mapToDto(client, ClientDto.class))
                .orElseThrow(() -> new EntityNotFoundException("Client", "numeroCompte", numeroCompte));
    }

    @Override
    @Transactional(readOnly = true)
    public ClientDto findByMatriculeFiscal(String matriculeFiscal) {
        log.info("Finding client by matricule fiscal: {}", matriculeFiscal);
        
        return clientRepository.findByMatriculeFiscal(matriculeFiscal)
                .map(client -> ObjectMapperUtils.mapToDto(client, ClientDto.class))
                .orElseThrow(() -> new EntityNotFoundException("Client", "matriculeFiscal", matriculeFiscal));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClientDto> findByNom(String nom) {
        log.info("Finding clients by nom containing: {}", nom);
        
        List<Client> clients = clientRepository.findByNomContainingIgnoreCase(nom);
        return ObjectMapperUtils.mapAll(clients, ClientDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClientDto> findBySecteurActiviteId(Long secteurActiviteId) {
        log.info("Finding clients by secteur activité ID: {}", secteurActiviteId);
        
        SecteurActivite secteurActivite = secteurActiviteRepository.findById(secteurActiviteId)
                .orElseThrow(() -> new EntityNotFoundException("SecteurActivite", "id", secteurActiviteId));
        
        List<Client> clients = clientRepository.findBySecteurActivite(secteurActivite);
        return ObjectMapperUtils.mapAll(clients, ClientDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClientDto> findByGouvernoratId(Long gouvernoratId) {
        log.info("Finding clients by gouvernorat ID: {}", gouvernoratId);
        
        Gouvernorat gouvernorat = gouvernoratRepository.findById(gouvernoratId)
                .orElseThrow(() -> new EntityNotFoundException("Gouvernorat", "id", gouvernoratId));
        
        List<Client> clients = clientRepository.findByGouvernorat(gouvernorat);
        return ObjectMapperUtils.mapAll(clients, ClientDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClientDto> findByExonere(Boolean exonere) {
        log.info("Finding clients by exoneré: {}", exonere);
        
        List<Client> clients = clientRepository.findByExonere(exonere);
        return ObjectMapperUtils.mapAll(clients, ClientDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClientDto> findAll() {
        log.info("Finding all clients");
        
        List<Client> clients = clientRepository.findAll();
        return ObjectMapperUtils.mapAll(clients, ClientDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClientDto> findAllOrderByDateCreationDesc() {
        log.info("Finding all clients ordered by creation date descending");
        
        List<Client> clients = clientRepository.findAllOrderByDateCreationDesc();
        return ObjectMapperUtils.mapAll(clients, ClientDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public long countAll() {
        log.info("Counting all clients");
        
        return clientRepository.countAllClients();
    }

    @Override
    @Transactional
    public ClientDto convertProspectToClient(ProspectDto prospectDto) {
        log.info("Converting prospect to client: {}", prospectDto.getNom());
        
        ClientDto clientDto = new ClientDto();
        clientDto.setNom(prospectDto.getNom());
        clientDto.setNumeroCompte(IdGenerator.generateClientAccountNumber());
        clientDto.setMobile(prospectDto.getMobile());
        clientDto.setTelephone(prospectDto.getTelephone());
        clientDto.setFax(prospectDto.getFax());
        clientDto.setEmail(prospectDto.getEmail());
        clientDto.setAutreEmail(prospectDto.getEmailSecondaire());
        clientDto.setSiteWeb(prospectDto.getSiteWeb());
        clientDto.setAdresseRue(prospectDto.getAdresseRue());
        clientDto.setAdresseCodePostal(prospectDto.getAdresseCodePostal());
        clientDto.setAdresseVille(prospectDto.getAdresseVille());
        clientDto.setAdressePays(prospectDto.getAdressePays());
        clientDto.setSecteurActivite(prospectDto.getSecteurActivite());
        clientDto.setChiffreAffaires(prospectDto.getChiffreAffaires());
        clientDto.setEffectif(prospectDto.getEffectif());
        
        return save(clientDto);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Deleting client with ID: {}", id);
        
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Client", "id", id));
        
        clientRepository.delete(client);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClientDto> findWithFilters(String nom, String matriculeFiscal, String email, 
                                         Long secteurActiviteId, String sourceProspection, 
                                         String statut, int page, int size) {
        log.info("Finding clients with filters - nom: {}, matriculeFiscal: {}, email: {}, secteurActiviteId: {}, page: {}, size: {}", 
                 nom, matriculeFiscal, email, secteurActiviteId, page, size);
        
        // Create a list to hold our filtering conditions
        List<Client> filteredClients = clientRepository.findAll();
        
        // Apply filters one by one
        if (nom != null && !nom.trim().isEmpty()) {
            filteredClients = filteredClients.stream()
                .filter(client -> client.getNom().toLowerCase().contains(nom.toLowerCase()))
                .toList();
        }
        
        if (matriculeFiscal != null && !matriculeFiscal.trim().isEmpty()) {
            filteredClients = filteredClients.stream()
                .filter(client -> client.getMatriculeFiscal() != null && 
                       client.getMatriculeFiscal().toLowerCase().contains(matriculeFiscal.toLowerCase()))
                .toList();
        }
        
        if (email != null && !email.trim().isEmpty()) {
            filteredClients = filteredClients.stream()
                .filter(client -> client.getEmail() != null && 
                       client.getEmail().toLowerCase().contains(email.toLowerCase()))
                .toList();
        }
        
        if (secteurActiviteId != null) {
            SecteurActivite secteurActivite = secteurActiviteRepository.findById(secteurActiviteId)
                    .orElse(null);
            
            if (secteurActivite != null) {
                filteredClients = filteredClients.stream()
                    .filter(client -> client.getSecteurActivite() != null && 
                           client.getSecteurActivite().getId().equals(secteurActiviteId))
                    .toList();
            }
        }
        
        // Apply pagination
        int fromIndex = page * size;
        int toIndex = Math.min(fromIndex + size, filteredClients.size());
        
        if (fromIndex >= filteredClients.size()) {
            return List.of(); // Empty list if page is beyond results
        }
        
        List<Client> pagedClients = filteredClients.subList(fromIndex, toIndex);
        
        return ObjectMapperUtils.mapAll(pagedClients, ClientDto.class);
    }
} 