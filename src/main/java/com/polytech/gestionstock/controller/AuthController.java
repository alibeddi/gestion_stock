package com.polytech.gestionstock.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.polytech.gestionstock.model.dto.UserDto;
import com.polytech.gestionstock.model.request.LoginRequest;
import com.polytech.gestionstock.model.response.ApiResponse;
import com.polytech.gestionstock.model.response.JwtAuthResponse;
import com.polytech.gestionstock.service.AuthService;
import com.polytech.gestionstock.service.LogoutService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Authentication", description = "Authentication operations for login, registration, and logout")
public class AuthController {

    private final AuthService authService;
    private final LogoutService logoutService;

    @Operation(
        summary = "Authenticate user",
        description = "Authenticates a user with email and password and returns a JWT token"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200", 
            description = "Authentication successful",
            content = @Content(schema = @Schema(implementation = JwtAuthResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid credentials"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Authentication failed")
    })
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtAuthResponse>> authenticateUser(
            @Parameter(description = "Login credentials", required = true)
            @Valid @RequestBody LoginRequest loginRequest) {
        
        log.info("Authentication request received for email: {}", loginRequest.getEmail());
        
        JwtAuthResponse jwt = authService.login(loginRequest.getEmail(), loginRequest.getPassword());
        
        return ResponseEntity.ok(ApiResponse.success(jwt, "User logged in successfully"));
    }

    @Operation(
        summary = "Register new user",
        description = "Registers a new user with the provided details"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201", 
            description = "User registered successfully",
            content = @Content(schema = @Schema(implementation = UserDto.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid user data or validation error"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Email already in use")
    })
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserDto>> registerUser(
            @Parameter(description = "User data for registration", required = true)
            @Valid @RequestBody UserDto userDto) {
        
        log.info("Registration request received for email: {}", userDto.getEmail());
        
        UserDto registeredUser = authService.register(userDto);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(registeredUser, "User registered successfully"));
    }
    
    @Operation(
        summary = "Logout user",
        description = "Logs out a user by invalidating their JWT token"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User logged out successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid token format")
    })
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logoutUser(
            @Parameter(description = "Authorization header with JWT token", required = true)
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        
        log.info("Logout request received");
        
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            logoutService.logout(jwt);
        }
        
        return ResponseEntity.ok(ApiResponse.success(null, "User logged out successfully"));
    }
}