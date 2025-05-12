package com.polytech.gestionstock.config;

import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.polytech.gestionstock.model.entity.Gouvernorat;
import com.polytech.gestionstock.model.entity.Role;
import com.polytech.gestionstock.model.entity.SecteurActivite;
import com.polytech.gestionstock.model.entity.User;
import com.polytech.gestionstock.repository.GouvernoratRepository;
import com.polytech.gestionstock.repository.RoleRepository;
import com.polytech.gestionstock.repository.SecteurActiviteRepository;
import com.polytech.gestionstock.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class DatabaseSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final GouvernoratRepository gouvernoratRepository;
    private final SecteurActiviteRepository secteurActiviteRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        seedRoles();
        seedUsers();
        seedGouvernorats();
        seedSecteursActivite();
    }

    private void seedRoles() {
        log.info("Checking for roles in the database...");
        
        if (roleRepository.count() == 0) {
            log.info("No roles found. Creating default roles...");
            
            Role adminRole = new Role();
            adminRole.setName("ROLE_ADMIN");
            adminRole.setLibelle("Administrator");
            roleRepository.save(adminRole);
            
            Role userRole = new Role();
            userRole.setName("ROLE_USER");
            userRole.setLibelle("Standard User");
            roleRepository.save(userRole);
            
            Role managerRole = new Role();
            managerRole.setName("ROLE_MANAGER");
            managerRole.setLibelle("Manager");
            roleRepository.save(managerRole);
            
            log.info("Default roles created successfully.");
        } else {
            log.info("Roles already exist in the database.");
        }
    }

    private void seedUsers() {
        log.info("Checking for admin user in the database...");
        
        if (!userRepository.existsByEmail("admin@example.com")) {
            log.info("Admin user not found. Creating default admin user...");
            
            Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                    .orElseThrow(() -> new RuntimeException("Admin role not found"));
            
            User adminUser = new User();
            adminUser.setNom("Admin");
            adminUser.setPrenom("Super");
            adminUser.setEmail("admin@example.com");
            adminUser.setPassword(passwordEncoder.encode("admin123"));
            adminUser.setEnabled(true);
            adminUser.setAccountNonExpired(true);
            adminUser.setAccountNonLocked(true);
            adminUser.setCredentialsNonExpired(true);
            
            Set<Role> roles = new HashSet<>();
            roles.add(adminRole);
            adminUser.setRoles(roles);
            
            userRepository.save(adminUser);
            
            log.info("Default admin user created successfully.");
        } else {
            log.info("Admin user already exists in the database.");
        }
        
        // Create a test user if it doesn't exist
        if (!userRepository.existsByEmail("user@example.com")) {
            log.info("Test user not found. Creating default test user...");
            
            Role userRole = roleRepository.findByName("ROLE_USER")
                    .orElseThrow(() -> new RuntimeException("User role not found"));
            
            User testUser = new User();
            testUser.setNom("User");
            testUser.setPrenom("Test");
            testUser.setEmail("user@example.com");
            testUser.setPassword(passwordEncoder.encode("password123"));
            testUser.setEnabled(true);
            testUser.setAccountNonExpired(true);
            testUser.setAccountNonLocked(true);
            testUser.setCredentialsNonExpired(true);
            
            Set<Role> roles = new HashSet<>();
            roles.add(userRole);
            testUser.setRoles(roles);
            
            userRepository.save(testUser);
            
            log.info("Default test user created successfully.");
        } else {
            log.info("Test user already exists in the database.");
        }
    }
    
    private void seedGouvernorats() {
        log.info("Checking for gouvernorats in the database...");
        
        if (gouvernoratRepository.count() == 0) {
            log.info("No gouvernorats found. Creating default gouvernorats for Tunisia...");
            
            String[][] gouvernoratsData = {
                {"01", "Tunis", "Tunisie"},
                {"02", "Ariana", "Tunisie"},
                {"03", "Ben Arous", "Tunisie"},
                {"04", "Manouba", "Tunisie"},
                {"05", "Nabeul", "Tunisie"},
                {"06", "Zaghouan", "Tunisie"},
                {"07", "Bizerte", "Tunisie"},
                {"08", "Béja", "Tunisie"},
                {"09", "Jendouba", "Tunisie"},
                {"10", "Le Kef", "Tunisie"},
                {"11", "Siliana", "Tunisie"},
                {"12", "Sousse", "Tunisie"},
                {"13", "Monastir", "Tunisie"},
                {"14", "Mahdia", "Tunisie"},
                {"15", "Sfax", "Tunisie"},
                {"16", "Kairouan", "Tunisie"},
                {"17", "Kasserine", "Tunisie"},
                {"18", "Sidi Bouzid", "Tunisie"},
                {"19", "Gabès", "Tunisie"},
                {"20", "Médenine", "Tunisie"},
                {"21", "Tataouine", "Tunisie"},
                {"22", "Gafsa", "Tunisie"},
                {"23", "Tozeur", "Tunisie"},
                {"24", "Kébili", "Tunisie"}
            };
            
            for (String[] data : gouvernoratsData) {
                Gouvernorat gouvernorat = new Gouvernorat();
                gouvernorat.setCode(data[0]);
                gouvernorat.setNom(data[1]);
                gouvernorat.setPays(data[2]);
                gouvernoratRepository.save(gouvernorat);
            }
            
            log.info("Default gouvernorats created successfully.");
        } else {
            log.info("Gouvernorats already exist in the database.");
        }
    }
    
    private void seedSecteursActivite() {
        log.info("Checking for secteurs d'activité in the database...");
        
        if (secteurActiviteRepository.count() == 0) {
            log.info("No secteurs d'activité found. Creating default secteurs...");
            
            String[][] secteursData = {
                {"AGR", "Agriculture", "Activités liées à l'agriculture et l'élevage"},
                {"IND", "Industrie", "Secteur industriel et manufacturier"},
                {"BAT", "Bâtiment et Travaux Publics", "Construction et travaux publics"},
                {"COM", "Commerce", "Commerce de détail et de gros"},
                {"TRS", "Transport", "Transport et logistique"},
                {"HEB", "Hôtellerie et Restauration", "Services d'hébergement et de restauration"},
                {"INF", "Information et Communication", "Médias, télécommunications et informatique"},
                {"FIN", "Activités Financières", "Banques, assurances et services financiers"},
                {"IMM", "Immobilier", "Activités immobilières"},
                {"PRO", "Services Professionnels", "Services spécialisés aux entreprises"},
                {"EDU", "Éducation", "Enseignement et formation"},
                {"SAN", "Santé", "Activités de santé humaine et action sociale"},
                {"ART", "Arts et Spectacles", "Activités artistiques et récréatives"},
                {"SER", "Services Divers", "Autres services aux particuliers"},
                {"ADM", "Administration Publique", "Administration et organismes publics"}
            };
            
            for (String[] data : secteursData) {
                SecteurActivite secteur = new SecteurActivite();
                secteur.setCode(data[0]);
                secteur.setLibelle(data[1]);
                secteur.setDescription(data[2]);
                secteurActiviteRepository.save(secteur);
            }
            
            log.info("Default secteurs d'activité created successfully.");
        } else {
            log.info("Secteurs d'activité already exist in the database.");
        }
    }
} 