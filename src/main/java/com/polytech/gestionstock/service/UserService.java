package com.polytech.gestionstock.service;

import java.util.List;

import com.polytech.gestionstock.model.dto.UserDto;

public interface UserService {
    UserDto save(UserDto userDto);
    UserDto update(Long id, UserDto userDto);
    UserDto findById(Long id);
    UserDto findByEmail(String email);
    List<UserDto> findAll();
    void delete(Long id);
} 