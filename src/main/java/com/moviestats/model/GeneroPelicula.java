package com.moviestats.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad que representa la tabla intermedia "MOVIE_GENRES" para la relación
 * muchos-a-muchos entre películas y géneros.
 * Esta clase mapea la tabla que asocia películas con sus géneros correspondientes
 * mediante claves foráneas movie_id y genre_id.
 *
 * Utiliza Lombok para generar automáticamente constructores, getters y setters.
 */
@Entity
@Table(name = "MOVIE_GENRES")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GeneroPelicula {

    /** Identificador único de la relación, generado automáticamente por la base de datos */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /** Identificador de la película asociada */
    @Column(name = "movie_id")
    private long movie_id;

    /** Identificador del género asociado */
    @Column(name = "genre_id")
    private long genre_id;
}
