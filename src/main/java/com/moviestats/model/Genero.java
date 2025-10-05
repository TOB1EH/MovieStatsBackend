package com.moviestats.model;

import java.util.List;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad que representa un género cinematográfico en el sistema MovieStats.
 * Esta clase mapea la tabla "GENRES" en la base de datos y define las categorías
 * de películas disponibles en la plataforma (ej: Acción, Drama, Comedia, etc.).
 *
 * Mantiene una relación bidireccional muchos-a-muchos con la entidad Pelicula.
 * Utiliza Lombok para generar automáticamente constructores, getters y setters.
 */
@Entity
@Table(name = "GENRES")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Genero {

    /** Identificador único del género, generado automáticamente por la base de datos */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idGenero;

    /** Nombre del género cinematográfico (ej: Acción, Drama, Comedia) */
    private String nombre;

    /** Lista de películas asociadas a este género (relación inversa muchos-a-muchos) */
    @ManyToMany(mappedBy = "genero")
    private List<Pelicula> peliculas;
}
