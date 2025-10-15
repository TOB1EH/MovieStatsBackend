package com.moviestats.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * Configuración de CORS (Cross-Origin Resource Sharing) para la aplicación.
 * Permite solicitudes desde el frontend Vue.js corriendo en localhost:5173.
 */
@Configuration
public class CorsConfig {

    /**
     * Bean que configura el filtro CORS.
     * Se crea una configuración que permite solicitudes desde el frontend,
     * incluyendo todos los métodos HTTP, headers y credenciales.
     *
     * @return instancia de CorsFilter configurada
     */
    @Bean
    public CorsFilter corsFilter() {
        // Fuente de configuración basada en URL para aplicar CORS
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // Permitir solicitudes desde el frontend Vue (nueva configuración para integración)
        config.addAllowedOrigin("http://localhost:5173"); // Puerto por defecto de Vite

        // Permitir todos los métodos HTTP (GET, POST, PUT, DELETE, etc.)
        config.addAllowedMethod("*");

        // Permitir todos los headers en las solicitudes
        config.addAllowedHeader("*");

        // Permitir credenciales (cookies, authorization headers) para autenticación
        config.setAllowCredentials(true);

        // Registrar la configuración para todas las rutas de la aplicación
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
