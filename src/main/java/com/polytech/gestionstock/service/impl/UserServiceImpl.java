package com.polytech.gestionstock.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.polytech.gestionstock.exception.DuplicateEntityException;
import com.polytech.gestionstock.exception.EntityNotFoundException;
import com.polytech.gestionstock.model.dto.PermissionDto;
import com.polytech.gestionstock.model.dto.UserDto;
import com.polytech.gestionstock.model.entity.Permission;
import com.polytech.gestionstock.model.entity.Role;
import com.polytech.gestionstock.model.entity.User;
import com.polytech.gestionstock.repository.PermissionRepository;
import com.polytech.gestionstock.repository.RoleRepository;
import com.polytech.gestionstock.repository.UserRepository;
import com.polytech.gestionstock.service.UserService;
import com.polytech.gestionstock.util.ObjectMapperUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final @Lazy PasswordEncoder passwordEncoder;
    private final PermissionRepository permissionRepository;

    @Override
    @Transactional
    public UserDto save(UserDto userDto) {
        log.info("Saving user with email: {}", userDto.getEmail());
        
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new DuplicateEntityException("User", "email", userDto.getEmail());
        }
        
        if (userDto.getMatricule() != null && userRepository.existsByMatricule(userDto.getMatricule())) {
            throw new DuplicateEntityException("User", "matricule", userDto.getMatricule());
        }
        
        User user = ObjectMapperUtils.mapToEntity(userDto, User.class);
        
        if (userDto.getPassword() != null && userDto.getPassword().equals(userDto.getConfirmPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        
        // Handle role assignment
        if (userDto.getRoles() == null || userDto.getRoles().isEmpty()) {
            // Check if there's a role field in the request
            if (userDto.getRole() != null && !userDto.getRole().isEmpty()) {
                String roleNameInput = userDto.getRole();
                // Check if role name already has ROLE_ prefix
                final String roleName = !roleNameInput.startsWith("ROLE_") 
                    ? "ROLE_" + roleNameInput 
                    : roleNameInput;
                
                Role role = roleRepository.findByName(roleName)
                        .orElseGet(() -> {
                            Role newRole = new Role();
                            newRole.setName(roleName);
                            newRole.setLibelle(roleName.replace("ROLE_", ""));
                            return roleRepository.save(newRole);
                        });
                
                Set<Role> roles = new HashSet<>();
                roles.add(role);
                user.setRoles(roles);
            } else {
                // Assign default USER role if no role specified
                Role userRole = roleRepository.findByName("ROLE_USER")
                        .orElseGet(() -> {
                            Role role = new Role();
                            role.setName("ROLE_USER");
                            role.setLibelle("Standard User");
                            return roleRepository.save(role);
                        });
                
                Set<Role> roles = new HashSet<>();
                roles.add(userRole);
                user.setRoles(roles);
            }
        }
        
        user = userRepository.save(user);
        return ObjectMapperUtils.mapToDto(user, UserDto.class);
    }

    @Override
    @Transactional
    public UserDto update(Long id, UserDto userDto) {
        log.info("Updating user with ID: {}", id);
        
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User", "id", id));
        
        // Check if email is being changed and if it's already taken
        if (!existingUser.getEmail().equals(userDto.getEmail()) && 
                userRepository.existsByEmail(userDto.getEmail())) {
            throw new DuplicateEntityException("User", "email", userDto.getEmail());
        }
        
        // Check if matricule is being changed and if it's already taken
        if (userDto.getMatricule() != null && 
                !userDto.getMatricule().equals(existingUser.getMatricule()) && 
                userRepository.existsByMatricule(userDto.getMatricule())) {
            throw new DuplicateEntityException("User", "matricule", userDto.getMatricule());
        }
        
        // Update the user details but preserve the original ID
        ObjectMapperUtils.updateEntityFromDto(userDto, existingUser);
        existingUser.setId(id);
        
        // Only update password if it's provided and confirmed
        if (userDto.getPassword() != null && !userDto.getPassword().isBlank() && 
                userDto.getPassword().equals(userDto.getConfirmPassword())) {
            existingUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        } else {
            // Keep the existing password if not being updated
            User persistedUser = userRepository.findById(id).orElseThrow();
            existingUser.setPassword(persistedUser.getPassword());
        }
        
        // Handle role updates if role field is provided
        if (userDto.getRole() != null && !userDto.getRole().isEmpty()) {
            String roleNameInput = userDto.getRole();
            // Check if role name already has ROLE_ prefix
            final String roleName = !roleNameInput.startsWith("ROLE_") 
                ? "ROLE_" + roleNameInput 
                : roleNameInput;
            
            Role role = roleRepository.findByName(roleName)
                    .orElseGet(() -> {
                        Role newRole = new Role();
                        newRole.setName(roleName);
                        newRole.setLibelle(roleName.replace("ROLE_", ""));
                        return roleRepository.save(newRole);
                    });
            
            Set<Role> roles = new HashSet<>();
            roles.add(role);
            existingUser.setRoles(roles);
        }
        
        existingUser = userRepository.save(existingUser);
        return ObjectMapperUtils.mapToDto(existingUser, UserDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto findById(Long id) {
        log.info("Finding user by ID: {}", id);
        
        return userRepository.findById(id)
                .map(user -> ObjectMapperUtils.mapToDto(user, UserDto.class))
                .orElseThrow(() -> new EntityNotFoundException("User", "id", id));
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto findByEmail(String email) {
        log.info("Finding user by email: {}", email);
        
        return userRepository.findByEmail(email)
                .map(user -> ObjectMapperUtils.mapToDto(user, UserDto.class))
                .orElseThrow(() -> new EntityNotFoundException("User", "email", email));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> findAll() {
        log.info("Finding all users");
        
        List<User> users = userRepository.findAll();
        return ObjectMapperUtils.mapAll(users, UserDto.class);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Deleting user with ID: {}", id);
        
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User", "id", id);
        }
        
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public UserDto assignPermissionsToUser(Long userId, Set<Long> permissionIds) {
        log.info("Assigning permissions {} to user with ID: {}", permissionIds, userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User", "id", userId));
        
        Set<Permission> permissionsToAdd = new HashSet<>();
        for (Long permissionId : permissionIds) {
            Permission permission = permissionRepository.findById(permissionId)
                    .orElseThrow(() -> new EntityNotFoundException("Permission", "id", permissionId));
            permissionsToAdd.add(permission);
        }
        
        // Add new permissions to the existing set
        user.getPermissions().addAll(permissionsToAdd);
        
        user = userRepository.save(user);
        
        return ObjectMapperUtils.mapToDto(user, UserDto.class);
    }

    @Override
    @Transactional
    public UserDto removePermissionsFromUser(Long userId, Set<Long> permissionIds) {
        log.info("Removing permissions {} from user with ID: {}", permissionIds, userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User", "id", userId));
        
        // Remove specified permissions
        user.setPermissions(user.getPermissions().stream()
                .filter(permission -> !permissionIds.contains(permission.getId()))
                .collect(Collectors.toSet()));
        
        user = userRepository.save(user);
        
        return ObjectMapperUtils.mapToDto(user, UserDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<PermissionDto> getUserPermissions(Long userId) {
        log.info("Getting permissions for user with ID: {}", userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User", "id", userId));
        
        // Combine permissions from roles and direct user permissions
        Set<Permission> allPermissions = new HashSet<>(user.getPermissions());
        
        user.getRoles().forEach(role -> {
            if (role.getPermissions() != null) {
                allPermissions.addAll(role.getPermissions());
            }
        });
        
        return allPermissions.stream()
                .map(permission -> ObjectMapperUtils.mapToDto(permission, PermissionDto.class))
                .collect(Collectors.toSet());
    }
} 