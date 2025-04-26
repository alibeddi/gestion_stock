package com.polytech.gestionstock.service;

import com.polytech.gestionstock.model.dto.UserDto;
import com.polytech.gestionstock.model.response.JwtAuthResponse;

public interface AuthService {
    JwtAuthResponse login(String email, String password);
    UserDto register(UserDto userDto);
} 