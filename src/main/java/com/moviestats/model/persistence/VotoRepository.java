package com.moviestats.model.persistence;

import java.util.Optional;

import com.moviestats.dto.VotoDTO;
import com.moviestats.model.Voto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la gestión de la persistencia de {@link Voto}.
 */
@Repository
public interface VotoRepository extends JpaRepository<Voto, Long> {
    
    /**
     * Busca los votos de un usuario por ids (usar los nombres reales de los campos idUsuario / idPelicula).
     */
    @Query("SELECT g FROM Voto g WHERE g.usuario.idUsuario = :usuario AND g.pelicula.idPelicula = :pelicula")
    Optional<Voto> findByIdUsuario(@Param("usuario") long usuario, @Param("pelicula") long pelicula);

    @Query("""
    SELECT new com.moviestats.dto.VotoDTO(g.idVoto, g.numero_voto)
    FROM Voto g
    WHERE g.usuario.idUsuario = :usuario AND g.pelicula.idPelicula = :pelicula
    """)
    Optional<VotoDTO> findByIdUsuarioDTO(@Param("usuario") long usuario, @Param("pelicula") long pelicula);


    /**
     * Busca el promedio de una pelicula en base a su id
     */
    @Query("SELECT AVG(v.numero_voto) FROM Voto v WHERE v.pelicula.idPelicula = :idPelicula")
    Float obtenerPromedioPorPeliula(@Param("idPelicula") Long idPelicula);

    /**
     * Busca la cantidad de votos de una película en base a su id
    */
    @Query("SELECT COUNT(v) FROM Voto v WHERE v.pelicula.idPelicula = :idPelicula")
    Integer obtenerCantidadDeVotosPorPelicula(@Param("idPelicula") Long idPelicula);

    /**
     * Busca un voto por id
     */
    @Query("SELECT g FROM Voto g WHERE g.idVoto = :voto")
    Optional<Voto> findById(@Param("voto") long voto);

}