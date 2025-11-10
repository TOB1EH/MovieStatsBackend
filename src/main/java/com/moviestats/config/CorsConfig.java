package com.moviestats.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

/**
 * Configuración de CORS (desarrollo): permite orígenes dinámicos mediante allowedOriginPatterns.
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // En desarrollo: permitir orígenes con patrones (incluye Postman/otras pestañas)
        config.setAllowedOriginPatterns(List.of("*"));

        // Permitir todos los métodos y headers
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");

        // Si necesitas credenciales desde el front
        config.setAllowCredentials(true);

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}