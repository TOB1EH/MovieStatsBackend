package com.moviestats.controller;

import com.moviestats.dto.LoginRequest;
import com.moviestats.dto.LoginResponse;
import com.moviestats.model.Usuario;
import com.moviestats.security.JwtUtil;
import com.moviestats.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.crypto.bcrypt.BCrypt;

/**
 * Controlador REST encargado de manejar las solicitudes relacionadas
 * con la autenticación de usuarios dentro del sistema MovieStats.
 * <p>
 * Este controlador expone los endpoints bajo la ruta base <b>/api/auth</b>,
 * permitiendo la validación de credenciales y la generación de tokens JWT
 * para sesiones autenticadas.
 * </p>
 *
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin // Permite solicitudes desde el frontend (por ejemplo, React, Angular, etc.)
public class AuthController {

    /**
     * Servicio de gestión de usuarios. Se utiliza para buscar
     * usuarios en la base de datos y validar credenciales.
     */
    @Autowired
    private UsuarioService usuarioService;

    /**
     * Utilidad para la generación y validación de tokens JWT.
     */
    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Endpoint para autenticar un usuario y generar un token JWT.
     * <p>
     * Este método recibe un objeto {@link LoginRequest} con las credenciales del usuario,
     * valida su formato y existencia, verifica la contraseña (utilizando BCrypt si está encriptada)
     * y finalmente genera un token JWT en caso de autenticación exitosa.
     * </p>
     *
     * @param loginRequest Objeto con los datos de inicio de sesión: correo y contraseña.
     * @return Un objeto {@link ResponseEntity} que contiene:
     * <ul>
     *     <li>{@link LoginResponse} con el token JWT en caso de éxito.</li>
     *     <li>Un mensaje de error en formato JSON en caso de fallo de autenticación o validación.</li>
     * </ul>
     *
     * <p><b>Códigos de respuesta HTTP:</b></p>
     * <ul>
     *     <li><b>200 OK:</b> Autenticación exitosa.</li>
     *     <li><b>400 Bad Request:</b> Datos faltantes o formato inválido.</li>
     *     <li><b>401 Unauthorized:</b> Credenciales incorrectas.</li>
     * </ul>
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        // Validación de datos de entrada
        if (loginRequest.getCorreo() == null || loginRequest.getCorreo().isBlank() ||
            loginRequest.getContrasenia() == null || loginRequest.getContrasenia().isBlank()) {
            return ResponseEntity.badRequest().body(
                "{\"error\": \"Email y contraseña son requeridos.\"}"
            );
        }

        // Validación del formato del correo electrónico
        if (!loginRequest.getCorreo().matches(".+@.+\\..+")) {
            return ResponseEntity.badRequest().body(
                "{\"error\": \"Formato de email inválido.\"}"
            );
        }

        // Búsqueda del usuario por correo electrónico
        Usuario usuario = usuarioService.findByCorreo(loginRequest.getCorreo()).orElse(null);
        if (usuario == null) {
            return ResponseEntity.status(401).body(
                "{\"error\": \"Usuario o contraseña incorrectos.\"}"
            );
        }

        // Verificar contraseña (usando bcrypt, pero si la contrasenia está en texto plano, usa equals)
        boolean passwordMatch;
        if (usuario.getContrasenia().startsWith("$2a$")) {
            passwordMatch = BCrypt.checkpw(loginRequest.getContrasenia(), usuario.getContrasenia());
        } else {
            passwordMatch = usuario.getContrasenia().equals(loginRequest.getContrasenia());
        }

        // Si la contraseña no coincide, se devuelve error de autenticación
        if (!passwordMatch) {
            return ResponseEntity.status(401).body(
                "{\"error\": \"Usuario o contraseña incorrectos.\"}"
            );
        }

        // Generación del token JWT y respuesta exitosa
        String token = jwtUtil.generateToken(usuario);
        return ResponseEntity.ok(new LoginResponse(token));
    }
}