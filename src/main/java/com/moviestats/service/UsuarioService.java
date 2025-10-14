package com.moviestats.service;

import com.moviestats.model.Usuario;
import com.moviestats.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    /**
     * Repositorio encargado de realizar las operaciones CRUD y consultas
     * personalizadas sobre la entidad {@link Usuario}.
     */
    @Autowired
    private UsuarioRepository usuarioRepository;

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