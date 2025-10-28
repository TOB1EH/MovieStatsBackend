package com.moviestats.model.persistence;

import java.util.Optional;

import com.moviestats.model.Voto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la gestión de la persistencia de {@link Voto}.
 * <p>
 * Extiende {@link JpaRepository} para proporcionar operaciones CRUD básicas y
 * consultas personalizadas sobre la entidad Voto.
 * </p>
 * 
 * <p>
 * Anotaciones:
 * <ul>
 *   <li>{@code @Repository}: Marca la interfaz como un componente de repositorio de Spring.</li>
 * </ul>
 * </p>
 */
@Repository
public interface VotoRepository extends JpaRepository<Voto, Long> {
    
    /**
     * Busca los votos de un usuario
     *
     * @param Voto Nombre o descripción del Voto a buscar.
     * @return {@link Optional} que contiene el Voto si se encuentra, o vacío si no existe.
     */
    @Query("SELECT g FROM Voto g WHERE g.idUsuario = :usuario AND g.idPelicula = :pelicula")
    Optional<Voto> findByIdUsuario(@Param("idUsuario") long usuario, @Param("idPelicula") String pelicula);

    /**
     * Busca el promedio de una pelicula en base a su id
     * @param Voto Nombre o descripción del Voto a buscar.
     * @param id Identificador del Voto a excluir de la búsqueda.
     * @return {@link Optional} que contiene el Voto si se encuentra, o vacío si no existe.
     */
    @Query("SELECT AVG(v.numero_voto) FROM Voto v WHERE v.peliula = :idPelicula")
    Double obtenerPromedioPorPeliula(@Param("idPelicula") String Voto);

}
