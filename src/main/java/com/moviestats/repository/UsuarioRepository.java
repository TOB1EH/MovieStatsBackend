package com.moviestats.repository;

import com.moviestats.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para la entidad Usuario.
 * Proporciona métodos para acceder y manipular datos de usuarios en la base de datos.
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Busca un usuario por su correo electrónico.
     * Este método es utilizado en el servicio de autenticación para verificar credenciales.
     *
     * @param correo el correo electrónico del usuario
     * @return un Optional con el usuario si existe, vacío si no
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
