package com.moviestats.service;

import com.moviestats.dto.RegisterRequest;
import com.moviestats.model.Usuario;
import com.moviestats.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Servicio para la gestión de usuarios.
 * Maneja la lógica de negocio relacionada con los usuarios, incluyendo el registro.
 * Esta clase es nueva y centraliza la lógica de registro de usuarios.
 */
@Service
public class UsuarioService {

    // Repositorio para acceder a la base de datos de usuarios
    @Autowired
    private UsuarioRepository usuarioRepository;

    // Encriptador de contraseñas para seguridad (inyectado desde SecurityConfig)
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * Registra un nuevo usuario en el sistema.
     * Hashea la contraseña, asigna el rol por defecto "USER" y verifica que el correo no esté duplicado.
     * Este método implementa la nueva funcionalidad de registro de usuarios.
     *
     * @param request los datos del usuario a registrar (DTO con validaciones)
     * @return el usuario registrado con ID generado
     * @throws IllegalArgumentException si el correo ya está registrado
     */
    public Usuario register(RegisterRequest request) {
        // Verificar si el correo ya existe (validación de unicidad)
        if (usuarioRepository.existsByCorreo(request.getEmail())) {
            throw new IllegalArgumentException("El correo electrónico ya está registrado");
        }

        // Crear nuevo usuario con los datos del request
        Usuario usuario = new Usuario();
        usuario.setNombre(request.getName());
        usuario.setApellido(request.getLast_name());
        usuario.setCorreo(request.getEmail());
        // Hashear la contraseña para almacenarla de forma segura
        usuario.setContrasenia(passwordEncoder.encode(request.getPassword()));
        usuario.setRol("USER"); // Rol por defecto asignado a nuevos usuarios

        // Guardar en la base de datos y devolver el usuario persistido
        return usuarioRepository.save(usuario);
    }
}
