package com.moviestats.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuración de seguridad para la aplicación.
 * Define beans necesarios para la autenticación y encriptación de contraseñas,
 * y configura las reglas de acceso a los endpoints.
 * Esta configuración habilita Spring Security y establece permisos para el registro público.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configura la cadena de filtros de seguridad.
     * Permite acceso público al endpoint de registro de usuarios (/api/auth/**),
     * y requiere autenticación para cualquier otro endpoint.
     * CSRF está deshabilitado para facilitar pruebas con herramientas como Postman.
     *
     * @param http el objeto HttpSecurity para configurar la seguridad web
     * @return la cadena de filtros configurada
     * @throws Exception si ocurre un error en la configuración
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable) // Deshabilitar CSRF para simplificar las pruebas con Postman (nueva configuración)
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/api/auth/**").permitAll() // Permitir acceso público al registro (nuevo endpoint público)
                .anyRequest().authenticated() // Requerir autenticación para otros endpoints (seguridad básica)
            );
        return http.build();
    }

    /**
     * Bean para el encriptador de contraseñas BCrypt.
     * Se utiliza para hashear las contraseñas de los usuarios antes de almacenarlas.
     * Este bean es inyectado en el UsuarioService para encriptar contraseñas.
     *
     * @return instancia de BCryptPasswordEncoder configurada
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
