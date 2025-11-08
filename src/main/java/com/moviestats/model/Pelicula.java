package com.moviestats.model;

import java.sql.Date;
import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad que representa una película en el sistema MovieStats.
 * Esta clase mapea la tabla "MOVIES" en la base de datos y contiene toda la información
 * detallada de las películas disponibles en la plataforma, incluyendo metadatos,
 * calificaciones y relaciones con géneros y actores.
 *
 * Utiliza Lombok para generar automáticamente constructores, getters y setters.
 */
@Entity
@Table(name = "MOVIES")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Pelicula {

    /** Identificador único de la película, generado automáticamente por la base de datos */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pelicula")
    private long idPelicula;

    /** Título de la película (máximo 300 caracteres para soportar títulos largos de TMDB) */
    @Column(length = 300, unique = false)
    private String nombre;

    /** Duración de la película en minutos */
    private int duracion;

    /** Nombre del director de la película (máximo 150 caracteres) */
    @Column(length = 150, unique = false)
    private String director;

    /** Resumen o descripción de la trama de la película (hasta 2000 caracteres para TMDB) */
    @Column(length = 2000)
    private String sinopsis;

    /** Idioma original de la película */
    @Column(length = 50)
    private String idioma;

    /** Clasificación por edad (ej: PG-13, R, etc.) */
    @Column(length = 50)
    private String clasificacion;

    /** Fecha de estreno de la película */
    private Date fechaSalida;

    /** Puntuación promedio calculada de la película */
    private Float puntuacion;

    /** Número total de votos recibidos por la película */
    private Integer votos;

    /** URL o ruta de la imagen del póster de la película (hasta 500 caracteres para URLs largas de TMDB) */
    @Column(length = 500)
    private String imagen;

    /** Lista de géneros asociados a la película (relación muchos a muchos) */
    @ManyToMany
    @JoinTable(
        name = "MOVIE_GENRES",
        joinColumns = @JoinColumn(name = "movie_id"),
        inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private List<Genero> genero;

    /** Lista de nombres de actores principales de la película */
    private List<String> actor;
}
