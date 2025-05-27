package com.polytech.gestionstock.service;

import java.util.List;
import java.util.Set;

import com.polytech.gestionstock.model.dto.PermissionDto;
import com.polytech.gestionstock.model.dto.UserDto;

public interface UserService {
    UserDto save(UserDto userDto);
    UserDto update(Long id, UserDto userDto);
    UserDto findById(Long id);
    UserDto findByEmail(String email);
    List<UserDto> findAll();
    void delete(Long id);
    UserDto assignPermissionsToUser(Long userId, Set<Long> permissionIds);
    UserDto removePermissionsFromUser(Long userId, Set<Long> permissionIds);
    Set<PermissionDto> getUserPermissions(Long userId);
} 