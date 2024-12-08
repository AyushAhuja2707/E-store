package com.ayush.estore.AyushStore.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {

    // @Bean
    // public io.swagger.v3.oas.models.OpenAPI customOpenAPI() {
    // return new io.swagger.v3.oas.models.OpenAPI()
    // .info(new Info()
    // .title("AyushStore API")
    // .version("1.0.0")
    // .description("Documentation for AyushStore APIs"));
    // }

    @Bean
    public OpenAPI customOpenAPI() {
        // Define the security scheme
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");

        // Apply the security scheme globally
        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList("JWT");

        return new OpenAPI()
                .info(new Info()
                        .title("AyushStore API")
                        .version("1.0.0")
                        .description("Documentation for AyushStore APIs"))
                .addSecurityItem(securityRequirement)
                .schemaRequirement("JWT", securityScheme);
    }
}
