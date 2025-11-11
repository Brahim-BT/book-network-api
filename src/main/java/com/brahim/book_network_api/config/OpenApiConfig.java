package com.brahim.book_network_api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(info = @Info(title = "Book Network API", version = "1.0", description = "API documentation for Book Network application"), servers = {
        @Server(url = "http://localhost:8081/api/v1", description = "Local server") }, security = {
                @SecurityRequirement(name = "bearerAuth") })
@SecurityScheme(name = "bearerAuth", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT", description = "JWT Bearer token authentication", in = SecuritySchemeIn.HEADER)
public class OpenApiConfig {

}
