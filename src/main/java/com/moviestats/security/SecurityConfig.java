package com.moviestats.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Clase de configuración de seguridad para el sistema MovieStats.
 * <p>
 * Define las reglas básicas de autorización y configuración de seguridad
 * mediante el uso del nuevo enfoque basado en beans de
 * {@link SecurityFilterChain}, introducido en Spring Security 6.
 * </p>
 *
 * <p>
 * Esta configuración deshabilita la protección CSRF (Cross-Site Request Forgery)
 * y establece qué rutas pueden ser accedidas sin autenticación. En este caso,
 * todas las solicitudes bajo <b>/api/auth/**</b> son públicas,
 * mientras que el resto de los endpoints requieren autenticación.
 * </p>
 *
 * <p><b>Uso principal:</b> Configurar los filtros y políticas
 * de seguridad HTTP aplicadas a la API.</p>
 *
 * @see org.springframework.security.web.SecurityFilterChain
 * @see com.moviestats.security.JwtUtil
 */
@Configuration
public class SecurityConfig {

    /**
     * Define la cadena de filtros de seguridad de Spring Security.
     * <p>
     * En esta configuración:
     * <ul>
     *   <li>Se deshabilita la protección CSRF, ya que las solicitudes se manejan por API REST (sin sesiones).</li>
     *   <li>Se permite el acceso libre a los endpoints de autenticación (<code>/api/auth/**</code>).</li>
     *   <li>Se exige autenticación para todas las demás solicitudes.</li>
     * </ul>
     * </p>
     *
     * <p><b>Nota:</b> Si en el futuro se implementa un filtro JWT para validar tokens,
     * deberá añadirse en esta configuración antes de construir la cadena de seguridad.</p>
     *
     * @param http objeto {@link HttpSecurity} que permite configurar
     *             las políticas de seguridad HTTP.
     * @return la cadena de filtros de seguridad construida.
     * @throws Exception si ocurre algún error al construir la configuración.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/api/auth/**").permitAll()
                .anyRequest().authenticated()
            );
        return http.build();
    }
}