package com.example.moviereviews.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(title = "Movie Review Platform API",
                version = "0.9.9",
                description = "Платоформа обзоров фильмов")
)
@Configuration
public class OpenApiConfig {
}
