package com.moviestats.service;

import com.moviestats.dto.RegisterRequest;
import com.moviestats.model.Usuario;
import com.moviestats.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Servicio de negocio encargado de gestionar la lógica relacionada
 * con los usuarios dentro del sistema MovieStats.
 * <p>
 * Este servicio actúa como una capa intermedia entre los controladores
 * y el repositorio de usuarios, delegando las operaciones de acceso
 * a datos a {@link UsuarioRepository}.
 * </p>
 *
 * <p><b>Uso principal:</b> Proporcionar métodos para la búsqueda y
 * manipulación de información de usuarios, garantizando una separación
 * adecuada entre la lógica de negocio y la capa de persistencia.</p>
 *
 * @see com.moviestats.repository.UsuarioRepository
 * @see com.moviestats.model.Usuario
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
  
    /**
     * Busca un usuario en la base de datos a partir de su dirección de correo electrónico.
     * <p>
     * Este método delega la consulta al repositorio y devuelve un objeto
     * {@link Optional} que puede contener el usuario si fue encontrado.
     * </p>
     *
     * <p><b>Ejemplo de uso:</b></p>
     * <pre>
     * Optional&lt;Usuario&gt; usuario = usuarioService.findByCorreo("usuario@ejemplo.com");
     * usuario.ifPresent(u -> System.out.println(u.getNombre()));
     * </pre>
     *
     * @param correo el correo electrónico del usuario a buscar.
     * @return un {@link Optional} con el usuario si existe, o vacío si no se encuentra.
     */
    public Optional<Usuario> findByCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }
}