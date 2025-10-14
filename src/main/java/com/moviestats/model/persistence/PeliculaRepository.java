package com.moviestats.model.persistence;

import java.util.Optional;

import com.moviestats.model.Pelicula;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la gestión de la persistencia de {@link Pelicula}.
 * <p>
 * Extiende {@link JpaRepository} para proporcionar operaciones CRUD básicas y
 * consultas personalizadas sobre la entidad pelicula.
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
public interface PeliculaRepository extends JpaRepository<Pelicula, Long> {
    
    /**
     * Busca un pelicula por su nombre o descripción exacta.
     *
     * @param pelicula Nombre o descripción del pelicula a buscar.
     * @return {@link Optional} que contiene el pelicula si se encuentra, o vacío si no existe.
     */
    @Query("SELECT g FROM movie g WHERE g.name = :pelicula OR g.description = :pelicula")
    Optional<Pelicula> findBypelicula(@Param("pelicula") String pelicula);

    /**
     * Busca un pelicula por su nombre, excluyendo un identificador específico.
     * Para encontrar al pelicula que no tenga el mismo id que se pasa por parámetro y así poder actualizarlo correctamente.
     *
     * @param pelicula Nombre o descripción del pelicula a buscar.
     * @param id Identificador del pelicula a excluir de la búsqueda.
     * @return {@link Optional} que contiene el pelicula si se encuentra, o vacío si no existe.
     */
    @Query("SELECT g FROM movie g WHERE (g.name = :pelicula OR g.description = :pelicula) AND g.id <> :id")
    Optional<Pelicula> findBypeliculaAndIdNot(@Param("pelicula") String pelicula, @Param("id") long id);

}
