package com.polytech.gestionstock.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.polytech.gestionstock.exception.DuplicateEntityException;
import com.polytech.gestionstock.model.dto.RoleDto;
import com.polytech.gestionstock.model.dto.UserDto;
import com.polytech.gestionstock.model.entity.Role;
import com.polytech.gestionstock.model.entity.User;
import com.polytech.gestionstock.model.response.JwtAuthResponse;
import com.polytech.gestionstock.repository.RoleRepository;
import com.polytech.gestionstock.repository.UserRepository;
import com.polytech.gestionstock.security.JwtTokenProvider;
import com.polytech.gestionstock.service.AuthService;
import com.polytech.gestionstock.service.UserService;
import com.polytech.gestionstock.util.ObjectMapperUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final @Lazy PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final UserService userService;

    @Override
    public JwtAuthResponse login(String email, String password) {
        log.info("Authenticating user with email: {}", email);
        
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        
        User user = (User) authentication.getPrincipal();
        
        String roles = user.getAuthorities().stream()
                .map(item -> item.getAuthority().replace("ROLE_", ""))
                .collect(Collectors.joining(", "));
                
        List<String> authorities = user.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        JwtAuthResponse response = new JwtAuthResponse();
        response.setAccessToken(jwt);
        response.setTokenType("Bearer");
        response.setUserId(user.getId());
        response.setUsername(user.getUsername());
        response.setName(user.getNom() + " " + user.getPrenom());
        response.setRoles(roles);
        response.setAuthorities(authorities);
        
        return response;
    }

    @Override
    @Transactional
    public UserDto register(UserDto userDto) {
        log.info("Registering new user with email: {}", userDto.getEmail());
        
        // Check if user already exists
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new DuplicateEntityException("User", "email", userDto.getEmail());
        }

        if (userDto.getMatricule() != null && userRepository.existsByMatricule(userDto.getMatricule())) {
            throw new DuplicateEntityException("User", "matricule", userDto.getMatricule());
        }

        // Encode password
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        
        // Assign default USER role if not specified
        if (userDto.getRoles() == null || userDto.getRoles().isEmpty()) {
            Set<RoleDto> roles = new HashSet<>();
            Role userRole = roleRepository.findByName("ROLE_USER")
                    .orElseGet(() -> {
                        Role role = new Role();
                        role.setName("ROLE_USER");
                        role.setLibelle("Standard User");
                        return roleRepository.save(role);
                    });
            roles.add(ObjectMapperUtils.mapToDto(userRole, RoleDto.class));
            userDto.setRoles(roles);
        }
        
        return userService.save(userDto);
    }
} 