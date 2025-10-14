package com.moviestats.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import com.moviestats.dto.RegisterRequest;
import com.moviestats.model.Usuario;
import com.moviestats.service.UsuarioService;
import java.util.Map;

/**
 * Controlador REST para la autenticación de usuarios.
 * Maneja las operaciones relacionadas con el registro y login de usuarios.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    // Inyección del servicio de usuarios para manejar la lógica de negocio
    @Autowired
    private UsuarioService usuarioService;

    /**
     * Endpoint para registrar un nuevo usuario.
     * Recibe los datos del usuario en el cuerpo de la solicitud y lo registra en el sistema.
     * Si el registro es exitoso, devuelve el usuario creado; si falla (ej. correo duplicado), devuelve un error.
     *
     * @param request los datos del usuario a registrar (DTO con nombre, apellido, email, password)
     * @return ResponseEntity con el usuario registrado (200 OK) o error (400 Bad Request)
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            // Llamar al servicio para registrar el usuario (nueva funcionalidad de registro)
            Usuario usuario = usuarioService.register(request);
            // Devolver el usuario registrado en caso de éxito
            return ResponseEntity.ok(usuario);
        } catch (IllegalArgumentException e) {
            // Manejar errores de validación (ej. correo ya registrado) y devolver mensaje de error
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
