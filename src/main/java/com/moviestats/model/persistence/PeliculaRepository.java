package com.moviestats.model.persistence;

import java.util.Optional;

import com.moviestats.model.Pelicula;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    @Query("SELECT g FROM Pelicula  g WHERE g.nombre = :pelicula OR g.sinopsis = :pelicula")
    Optional<Pelicula> findByNombre(@Param("pelicula") String pelicula);

    /**
     * Busca un pelicula por su nombre, excluyendo un identificador específico.
     * Para encontrar al pelicula que no tenga el mismo id que se pasa por parámetro y así poder actualizarlo correctamente.
     *
     * @param pelicula Nombre o descripción del pelicula a buscar.
     * @param id Identificador del pelicula a excluir de la búsqueda.
     * @return {@link Optional} que contiene el pelicula si se encuentra, o vacío si no existe.
     */
    @Query("SELECT g FROM Pelicula  g WHERE (g.nombre = :pelicula OR g.sinopsis = :pelicula) AND g.idPelicula <> :id")
    Optional<Pelicula> findByNombreAndIdNot(@Param("pelicula") String pelicula, @Param("id") long id);

    /**
     * Búsqueda avanzada con filtros opcionales y paginación.
     * <p>
     * Esta consulta permite filtrar películas utilizando múltiples criterios de búsqueda que pueden combinarse.
     * Todos los parámetros son opcionales (pueden ser null o vacíos) y se aplican solo si tienen valor.
     * </p>
     * 
     * <h3>Filtros disponibles:</h3>
     * <ul>
     *   <li><b>q</b>: Búsqueda por texto en el nombre de la película (case-insensitive, búsqueda parcial)</li>
     *   <li><b>genre</b>: Filtro exacto por nombre de género</li>
     *   <li><b>year</b>: Filtro por año de salida de la película</li>
     *   <li><b>minRating</b>: Filtro por puntuación mínima</li>
     * </ul>
     * 
     * <h3>Ejemplo de uso:</h3>
     * <pre>
     * // Buscar películas de acción del 2024 con puntuación >= 8.0 que contengan "batman" en el nombre
     * findByFilters("batman", "Acción", 2024, 8.0f, PageRequest.of(0, 10));
     * </pre>
     * 
     * <h3>Sobre el countQuery:</h3>
     * <p>
     * El <b>countQuery</b> es necesario para calcular el total de elementos que cumplen los filtros,
     * sin aplicar límites de paginación. Spring Data lo usa para:
     * </p>
     * <ul>
     *   <li>Calcular {@code totalElements}: número total de películas que cumplen los criterios</li>
     *   <li>Calcular {@code totalPages}: número total de páginas disponibles</li>
     *   <li>Determinar si hay más datos para cargar (útil para botones "Cargar más")</li>
     * </ul>
     * 
     * <h3>Detalles técnicos:</h3>
     * <ul>
     *   <li>Usa COALESCE para manejar valores null en String sin errores de tipo en PostgreSQL</li>
     *   <li>Usa EXTRACT(YEAR FROM ...) en lugar de YEAR() para compatibilidad con PostgreSQL</li>
     *   <li>LEFT JOIN con géneros para incluir películas sin género asignado</li>
     *   <li>DISTINCT evita duplicados cuando una película tiene múltiples géneros</li>
     * </ul>
     *
     * @param q Texto de búsqueda para filtrar por nombre de película (opcional)
     * @param genre Nombre del género para filtrar (opcional)
     * @param year Año de salida para filtrar (opcional)
     * @param minRating Puntuación mínima requerida (opcional)
     * @param pageable Configuración de paginación (página, tamaño, ordenamiento)
     * @return Page con las películas que cumplen los criterios, incluyendo información de paginación
     */
    @Query(value = "SELECT DISTINCT p FROM Pelicula p LEFT JOIN p.genero g " +
                   "WHERE (COALESCE(:q, '') = '' OR LOWER(p.nombre) LIKE CONCAT('%', LOWER(:q), '%')) " +
                   "AND (COALESCE(:genre, '') = '' OR g.nombre = :genre) " +
                   "AND (:year IS NULL OR EXTRACT(YEAR FROM p.fechaSalida) = :year) " +
                   "AND (:minRating IS NULL OR p.puntuacion >= :minRating)",
           countQuery = "SELECT COUNT(DISTINCT p) FROM Pelicula p LEFT JOIN p.genero g " +
                   "WHERE (COALESCE(:q, '') = '' OR LOWER(p.nombre) LIKE CONCAT('%', LOWER(:q), '%')) " +
                   "AND (COALESCE(:genre, '') = '' OR g.nombre = :genre) " +
                   "AND (:year IS NULL OR EXTRACT(YEAR FROM p.fechaSalida) = :year) " +
                   "AND (:minRating IS NULL OR p.puntuacion >= :minRating)")
    Page<Pelicula> findByFilters(@Param("q") String q,
                                @Param("genre") String genre,
                                @Param("year") Integer year,
                                @Param("minRating") Float minRating,
                                Pageable pageable);

}
