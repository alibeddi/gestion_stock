package com.polytech.gestionstock.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;

@Configuration
public class OpenApiConfig {

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";

        // Define API tags
        List<Tag> tags = Arrays.asList(
            createTag("Authentication", "Operations related to user authentication"),
            createTag("Users", "Operations for user management"),
            createTag("Roles", "Operations for role management"),
            createTag("Clients", "Operations for client management"),
            createTag("Prospects", "Operations for prospect management"),
            createTag("Contacts", "Operations for contact management"),
            createTag("Products", "Operations for product management"),
            createTag("Packaging", "Operations for packaging/emballage management"),
            createTag("Quotes", "Operations for quotes/devis management"),
            createTag("Geographic", "Operations for geographic data (gouvernorats)"),
            createTag("Business Sectors", "Operations for business sector management"),
            createTag("Prospection Sources", "Operations for prospection source management")
        );
        
        return new OpenAPI()
                .info(
                    new Info()
                        .title("Gestion Stock API")
                        .description(
                            "Customer Relationship Management (CRM) API for stock and client management. " +
                            "This API provides endpoints for managing clients, prospects, products, quotes, and related entities. " +
                            "Authenticated access is required for most endpoints, except for the authentication endpoints."
                        )
                        .version("1.0.0")
                        .contact(
                            new Contact()
                                .name("Polytech Team")
                                .email("contact@polytech.com")
                                .url("https://polytech.com")
                        )
                        .license(new License().name("MIT").url("https://opensource.org/licenses/MIT"))
                        .termsOfService("https://polytech.com/terms")
                )
                .externalDocs(
                    new ExternalDocumentation()
                        .description("CRM System Documentation")
                        .url("https://polytech.com/docs")
                )
                .servers(Arrays.asList(
                    new Server()
                        .url("http://localhost:8080" + contextPath)
                        .description("Local Development Server")
                ))
                .tags(tags)
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(
                    new Components()
                        .addSecuritySchemes(securitySchemeName,
                            new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("JWT Authentication using Bearer token. Format: 'Bearer <token>'")
                        )
                );
    }

    private Tag createTag(String name, String description) {
        return new Tag().name(name).description(description);
    }
} 