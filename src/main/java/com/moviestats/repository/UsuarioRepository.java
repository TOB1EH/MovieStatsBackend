package com.moviestats.repository;

import com.moviestats.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio encargado de gestionar las operaciones de
 * persistencia y consulta de la entidad {@link Usuario}
 * en la base de datos del sistema MovieStats.
 * <p>
 * Esta interfaz extiende {@link JpaRepository}, lo que proporciona
 * automáticamente un conjunto completo de operaciones CRUD,
 * paginación y ordenamiento. Además, define un método personalizado
 * para la búsqueda de usuarios por correo electrónico.
 * </p>
 *
 * <p><b>Uso principal:</b> Es utilizada por la capa de servicio
 * {@code UsuarioService} para acceder a la información de los usuarios.</p>
 *
 * <p><b>Ejemplo de uso:</b></p>
 * <pre>
 * Optional&lt;Usuario&gt; usuario = usuarioRepository.findByCorreo("usuario@ejemplo.com");
 * </pre>
 *
 * @see com.moviestats.model.Usuario
 * @see com.moviestats.service.UsuarioService
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    /**
     * Busca un usuario en la base de datos a partir de su dirección de correo electrónico.
     *
     * @param correo el correo electrónico del usuario a buscar.
     * @return un {@link Optional} que contiene el usuario si fue encontrado,
     *         o vacío si no existe ningún usuario con ese correo.
     */
    Optional<Usuario> findByCorreo(String correo);
  
    /**
     * Verifica si existe un usuario con el correo electrónico dado.
     * Se utiliza durante el registro para evitar duplicados de correos.
     *
     * @param correo el correo electrónico a verificar
     * @return true si existe, false si no
     */
    boolean existsByCorreo(String correo);
}