# Gestion Stock – Spring Boot Backend

## Project Overview

**Gestion Stock** is a backend system built with Spring Boot for managing inventory, users, authentication, and related business operations. It provides a RESTful API for client applications (web, mobile, etc.) to interact with the system securely and efficiently.

---

## Architecture & Main Components

The project follows a layered architecture for maintainability and scalability:

- **Controller Layer**: Handles HTTP requests and responses, API documentation, and input validation.
- **Service Layer**: Contains business logic and orchestrates data flow between controllers and repositories.
- **Repository Layer**: Interfaces with the database using Spring Data JPA.
- **Model Layer**: Defines entities (database tables), DTOs (data transfer objects), and request/response models.
- **Security Layer**: Manages authentication (JWT), authorization, and user session management.
- **Configuration Layer**: Contains configuration classes for security, database seeding, and other application settings.

---

## Key Features

- **User Authentication**: Register, login, and logout with JWT-based security.
- **Role-Based Access Control**: Different user roles (admin, user, manager) with specific permissions.
- **Inventory Management**: CRUD operations for products, packaging (emballage), and clients.
- **API Documentation**: Swagger/OpenAPI integration for easy API exploration.
- **Validation & Error Handling**: Input validation and standardized API responses.

---

## API Endpoints

Some of the main endpoints (see Swagger UI for full details):

- `POST /api/auth/login` – Authenticate user and receive JWT.
- `POST /api/auth/register` – Register a new user.
- `POST /api/auth/logout` – Logout and invalidate JWT.
- `GET /api/products` – List all products.
- `POST /api/products` – Add a new product.
- `GET /api/clients` – List all clients.
- ...and more for managing emballage, roles, users, etc.

**API documentation** is available at `/swagger-ui.html` or `/v3/api-docs`.

---

## Setup & Running Locally

### Prerequisites

- Java 17+
- Maven 3.6+
- (Optional) MySQL/PostgreSQL if using a real database

### Steps

1. **Clone the repository**

   ```bash
   git clone <your-repo-url>
   cd new-gestion-stock
   ```

2. **Configure the database**

   - Edit `src/main/resources/application.properties` to set your DB connection.
   - By default, H2 in-memory DB may be used for development.

3. **Build the project**

   ```bash
   mvn clean install
   ```

4. **Run the application**

   ```bash
   mvn spring-boot:run
   ```

   The server will start on [http://localhost:8080](http://localhost:8080).

5. **Access Swagger UI**
   - Visit [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) for API docs.

---

## Project Structure

```src/main/java/com/polytech/gestionstock/
├── config/ # Configuration classes (security, database seeding, etc.)
├── controller/ # REST controllers (API endpoints)
├── model/ # Entities, DTOs, requests, responses
│ ├── entity/
│ ├── dto/
│ ├── request/
│ └── response/
├── repository/ # Spring Data JPA repositories
├── security/ # Security configuration and JWT utilities
├── service/ # Business logic (interfaces and implementations)
└── GestionStockApplication.java # Main Spring Boot application
```

---

## Technologies Used

- **Spring Boot** (REST API, dependency injection)
- **Spring Data JPA** (database access)
- **Spring Security** (authentication/authorization)
- **JWT** (stateless authentication)
- **Lombok** (boilerplate code reduction)
- **Swagger/OpenAPI** (API documentation)
- **H2/MySQL/PostgreSQL** (database)
- **Maven** (build tool)

---

## Development & Contribution

1. Fork the repository and create your feature branch.
2. Commit your changes with clear messages.
3. Push to your branch and open a Pull Request.
4. Ensure your code passes all tests and follows the project's style.

---

## Troubleshooting

- **Compilation errors about missing methods/fields**: Ensure all source files are present and up-to-date. If using Lombok, install the Lombok plugin in your IDE.
- **Database connection issues**: Check your `application.properties` for correct DB credentials.
- **Swagger not loading**: Make sure the app is running and you're using the correct URL.

---

## Contact

For questions, suggestions, or bug reports, please open an issue or contact the maintainer at [your-email@example.com].
