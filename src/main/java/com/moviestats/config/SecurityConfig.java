package com.moviestats.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuración de seguridad unificada para la aplicación MovieStats.
 * <p>
 * Define beans necesarios para la autenticación y encriptación de contraseñas,
 * y configura las reglas de acceso a los endpoints.
 * </p>
 * <p>
 * Características:
 * <ul>
 *   <li>Habilita seguridad a nivel de método con {@link EnableMethodSecurity}</li>
 *   <li>Permite acceso público a endpoints de autenticación y películas</li>
 *   <li>CSRF deshabilitado para APIs REST</li>
 *   <li>Proporciona BCryptPasswordEncoder para encriptación de contraseñas</li>
 * </ul>
 * </p>
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    /**
     * Configura la cadena de filtros de seguridad.
     * <p>
     * Permite acceso público a:
     * <ul>
     *   <li>/api/auth/** - Endpoints de autenticación (registro, login)</li>
     *   <li>/api/v1/pelicula/** - Endpoints de películas (listado público)</li>
     * </ul>
     * Requiere autenticación para cualquier otro endpoint.
     * CSRF está deshabilitado para facilitar el consumo de la API REST.
     * </p>
     *
     * @param http el objeto HttpSecurity para configurar la seguridad web
     * @return la cadena de filtros configurada
     * @throws Exception si ocurre un error en la configuración
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/api/auth/**").permitAll()           // Autenticación pública
                .requestMatchers("/api/v1/pelicula/**").permitAll()    // Películas públicas
                .requestMatchers("/api/v1/genero/**").permitAll()      // Géneros públicos
                .anyRequest().authenticated()                          // Resto requiere autenticación
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
