# CRM - Gestion Stock Application

A Customer Relationship Management (CRM) application built with Spring Boot (backend) and Angular (frontend).

## Project Structure

The project is divided into two main parts:

- **Backend**: Spring Boot application
- **Frontend**: Angular application

### Backend Structure

```
src/main/java/com/polytech/gestionstock/
├── config/               # Configuration classes
├── controller/           # REST API controllers
├── exception/            # Custom exceptions and error handling
├── model/                # Data models
│   ├── dto/              # Data Transfer Objects
│   ├── entity/           # JPA Entities
│   ├── request/          # Request models
│   └── response/         # Response models
├── repository/           # Spring Data JPA repositories
├── security/             # Security configuration and JWT handling
├── service/              # Business logic
│   └── impl/             # Service implementations
└── util/                 # Utility classes
```

### Main Features

- User Authentication and Authorization with JWT
- Role-based access control
- CRUD operations for all entities:
  - User and Role management
  - Prospect management
  - Client management
  - Contact management
  - Emballage (Packaging) management
  - Product management
  - Devis (Quote/Estimate) management
- Configuration management (Secteurs d'activité, Gouvernorats, etc.)

## Getting Started

### Prerequisites

- Java 17+
- Maven
- MySQL Database
- Node.js and npm (for Angular frontend)

### Backend Setup

1. Clone the repository

   ```
   git clone https://github.com/yourusername/gestion-stock.git
   cd gestion-stock
   ```

2. Configure the database connection in `src/main/resources/application.properties`

3. Build and run the application
   ```
   mvn clean install
   mvn spring-boot:run
   ```

The backend API will be available at http://localhost:8080/api

### API Documentation

The API documentation is generated using SpringDoc OpenAPI and can be accessed at:

- http://localhost:8080/api/swagger-ui.html
- http://localhost:8080/api/api-docs

## Database Schema

The database schema includes the following main tables:

- users
- roles
- prospects
- clients
- contacts
- emballages
- produits
- devis
- lignes_devis
- secteurs_activite
- gouvernorats
- sources_prospection

## Security

The application uses Spring Security with JWT (JSON Web Tokens) for authentication and authorization.

## License

This project is licensed under the MIT License - see the LICENSE file for details.
