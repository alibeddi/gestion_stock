package com.polytech.gestionstock.config;

import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.polytech.gestionstock.model.entity.Role;
import com.polytech.gestionstock.model.entity.User;
import com.polytech.gestionstock.repository.RoleRepository;
import com.polytech.gestionstock.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class DatabaseSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        seedRoles();
        seedUsers();
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
} 