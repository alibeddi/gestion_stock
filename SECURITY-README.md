# Security Implementation Documentation

## Overview

The `/src/main/java/com/gestionstock/security` folder contains the security implementation for the Gestion Stock application. This module is responsible for handling authentication, authorization, and securing API endpoints using JSON Web Tokens (JWT).

## Components

The security module consists of the following key components:

### 1. SecurityConfig.java

This is the main security configuration class that sets up Spring Security for the application:

- Configures security filters and authentication mechanisms
- Defines protected and public endpoints
- Sets up CORS (Cross-Origin Resource Sharing) configuration
- Configures stateless session management using JWT
- Provides beans for password encoding and authentication management

Key features:

- Permits public access to `/api/auth/**` endpoints (for login/registration)
- Permits access to Swagger documentation
- Requires authentication for all other API endpoints
- Uses BCrypt for password encoding
- Configures CORS to allow requests from the Angular frontend (http://localhost:4200)

### 2. JwtTokenProvider.java

Responsible for JWT token generation, validation, and parsing:

- Generates JWT tokens upon successful authentication
- Extracts user information from JWT tokens
- Validates token signatures and expiration
- Uses a secret key defined in application properties
- Includes user roles, ID, and name in the token payload

### 3. JwtAuthenticationFilter.java

Intercepts incoming requests to validate JWT tokens:

- Extracts JWT token from the Authorization header
- Validates the token using JwtTokenProvider
- Checks if the token has been invalidated (logged out)
- Loads user details and sets up the security context
- Allows the request to proceed if the token is valid

### 4. JwtAuthenticationEntryPoint.java

Handles unauthorized access attempts:

- Triggered when an unauthenticated user tries to access a protected resource
- Returns a proper HTTP 401 Unauthorized response with a JSON payload
- Logs unauthorized access attempts

### 5. SecurityUtils.java

Utility class providing helper methods for security-related operations:

- Checks if the current authenticated user is accessing their own data
- Retrieves the currently authenticated user from the security context

## Authentication Flow

1. User submits credentials to `/api/auth/login`
2. Upon successful authentication, a JWT token is generated and returned
3. The client includes this token in the Authorization header for subsequent requests
4. JwtAuthenticationFilter validates the token for each request
5. If valid, the user is authenticated and granted access to protected resources

## Security Features

- **Stateless Authentication**: No server-side sessions, improving scalability
- **Token-Based Security**: Using industry-standard JWT for secure authentication
- **Password Encryption**: BCrypt hashing for secure password storage
- **Role-Based Access Control**: Endpoints can be secured based on user roles
- **CORS Configuration**: Prevents unauthorized cross-origin requests
- **CSRF Protection**: Disabled for stateless API (not needed with proper JWT implementation)
- **Token Invalidation**: Support for logging out by invalidating tokens

## Configuration

The JWT configuration parameters are defined in the application properties:

- `jwt.secret`: The secret key used for signing tokens
- `jwt.expiration`: Token validity period in milliseconds

## Best Practices

- Keep the JWT secret key secure and use environment variables in production
- Implement token refresh mechanisms for long-lived sessions
- Consider using shorter expiration times for sensitive operations
- Monitor and log authentication failures for security auditing
