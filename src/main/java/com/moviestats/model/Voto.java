package com.moviestats.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad que representa un voto de un usuario a una película en el sistema MovieStats.
 * Esta clase mapea la tabla "VOTES" en la base de datos y registra las calificaciones
 * que los usuarios asignan a las películas, permitiendo calcular puntuaciones promedio.
 *
 * Mantiene relaciones muchos-a-uno con Usuario y Pelicula.
 * Utiliza Lombok para generar automáticamente constructores, getters y setters.
 */
@Entity
@Table(name = "VOTES")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Voto {

    /** Identificador único del voto, generado automáticamente por la base de datos */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idVoto;

    /** Usuario que emitió el voto (relación muchos-a-uno) */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private Usuario usuario;

    /** Película que recibió el voto (relación muchos-a-uno) */
    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Pelicula pelicula;

    /** Valor numérico del voto (típicamente en escala 1-10 o 1-5) */
    private int numero_voto;
}
