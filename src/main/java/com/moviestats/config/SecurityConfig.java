package com.moviestats.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

/**
 * Configuración de seguridad unificada para la aplicación MovieStats.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Habilita CORS para que Spring Security respete la configuración de CORS
            .cors().and()
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(authz -> authz
                // Permitir preflight OPTIONS
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                // si quieres permitir todo /api/v1 puedes mantener la siguiente línea
                .requestMatchers("/api/auth/**").permitAll()           // Autenticación pública
                .requestMatchers("/api/v1/pelicula/**").permitAll()    // Películas públicas
                .requestMatchers("/api/v1/genero/**").permitAll()      // Géneros públicos
                .requestMatchers("/api/v1/tmdb/**").permitAll()        // TMDB sync endpoints públicos
                .requestMatchers("/api/v1/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v1/voto/**").permitAll()
                // .anyRequest().authenticated()                          // Resto requiere autenticación
            );
        return http.build();
    }

    /**
     * Bean para el encriptador de contraseñas BCrypt.
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Fuente de configuración CORS utilizada por Spring Security.
     * Ajusta allowedOrigin(s) según tus orígenes de frontend.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        // permitir el origen del frontend (o usa config.addAllowedOriginPattern("*") en desarrollo)
        config.setAllowedOrigins(List.of("http://localhost:5173"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}