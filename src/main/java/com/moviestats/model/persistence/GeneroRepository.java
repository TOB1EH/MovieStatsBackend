package com.moviestats.model.persistence;

import java.util.Optional;

import com.moviestats.model.Genero;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la gestión de la persistencia de {@link Genero}.
 * <p>
 * Extiende {@link JpaRepository} para proporcionar operaciones CRUD básicas y
 * consultas personalizadas sobre la entidad genero.
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
public interface GeneroRepository extends JpaRepository<Genero, Long> {
    
    /**
     * Busca un genero por su nombre o descripción exacta.
     *
     * @param genero Nombre o descripción del genero a buscar.
     * @return {@link Optional} que contiene el genero si se encuentra, o vacío si no existe.
     */
    @Query("SELECT g FROM Genero g WHERE g.nombre = :genero")
    Optional<Genero> findByNombre(@Param("genero") String genero);

    /**
     * Busca un genero por su nombre, excluyendo un identificador específico.
     * Para encontrar al genero que no tenga el mismo id que se pasa por parámetro y así poder actualizarlo correctamente.
     *
     * @param genero Nombre o descripción del genero a buscar.
     * @param id Identificador del genero a excluir de la búsqueda.
     * @return {@link Optional} que contiene el genero si se encuentra, o vacío si no existe.
     */
    @Query("SELECT g FROM Genero g WHERE g.nombre = :genero AND g.idGenero <> :id")
    Optional<Genero> findByNombreAndIdNot(@Param("genero") String genero, @Param("id") long id);

}
