package com.moviestats.security;

import com.moviestats.model.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * Clase de utilidad para la generación de tokens JWT (JSON Web Tokens)
 * utilizados en la autenticación y autorización dentro del sistema MovieStats.
 * <p>
 * Esta clase crea tokens firmados utilizando el algoritmo HS256 y una
 * clave secreta segura. Los tokens generados contienen información básica
 * del usuario, como su correo, nombre, apellido y rol.
 * </p>
 *
 * <p><b>Uso principal:</b> Es utilizada por el controlador
 * {@code AuthController} al momento de autenticar un usuario,
 * generando el token JWT que será devuelto al cliente.</p>
 *
 * <p><b>Seguridad:</b> La clave secreta debe mantenerse privada y no ser
 * expuesta en el código fuente en entornos de producción. Se recomienda
 * almacenarla en variables de entorno o un servicio de configuración segura.</p>
 *
 * @see com.moviestats.controller.AuthController
 * @see com.moviestats.model.Usuario
 */
@Component
public class JwtUtil {

    /**
     * Clave secreta utilizada para firmar los tokens JWT.
     * <p>
     * Debe tener una longitud mínima de 32 caracteres para el algoritmo HS256.
     * En entornos de producción, esta clave debe almacenarse en una variable
     * de entorno y no directamente en el código fuente.
     * </p>
     */
    private static final String SECRET = "moviestats-super-secret-key-2025-12345678901234567890123456789012";

    /**
     * Llave criptográfica generada a partir de la clave secreta.
     * Se utiliza por la librería {@link io.jsonwebtoken.Jwts} para firmar el token.
     */
    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

    /**
     * Genera un token JWT firmado que contiene la información básica del usuario autenticado.
     * <p>
     * El token incluye los siguientes campos (claims):
     * <ul>
     *     <li><b>sub</b>: correo electrónico del usuario (identificador principal).</li>
     *     <li><b>nombre</b>: nombre del usuario.</li>
     *     <li><b>apellido</b>: apellido del usuario.</li>
     *     <li><b>rol</b>: rol del usuario dentro del sistema.</li>
     *     <li><b>iat</b>: fecha de emisión del token.</li>
     *     <li><b>exp</b>: fecha de expiración (24 horas después de la emisión).</li>
     * </ul>
     * </p>
     *
     * @param usuario el objeto {@link Usuario} del cual se extraerá la información
     *                para incluir en el token JWT.
     * @return un {@link String} que representa el token JWT firmado.
     */
    public String generateToken(Usuario usuario) {
        Instant now = Instant.now();
        Instant expiresAt = now.plus(24, ChronoUnit.HOURS);

        JwtBuilder builder = Jwts.builder()
                .subject(usuario.getCorreo())
                .claim("nombre", usuario.getNombre())
                .claim("apellido", usuario.getApellido())
                .claim("rol", usuario.getRol())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiresAt))
                .signWith(key); // Ya no se pasa algoritmo, se deduce por la clave

        return builder.compact();
    }
}