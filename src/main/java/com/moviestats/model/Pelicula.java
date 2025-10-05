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

@Entity
@Table(name = "MOVIES")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Pelicula {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idPelicula;

    @Column(length = 100, unique = false)
    private String nombre;
    
    private int duracion;

    @Column(length = 100, unique = false)
    private String director;

    private String sinopsis;

    private String idioma;

    private String clasificacion;

    private Date fechaSalida;

    private float puntuacion;

    private int votos;

    private String imagen;

    @ManyToMany
    @JoinTable(
        name = "MOVIE_GENRES",
        joinColumns = @JoinColumn(name = "movie_id"),
        inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private List<Genero> genero;

    private List<String> actor;
}
