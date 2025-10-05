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

@Entity
@Table(name = "GENRES")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Genero {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idGenero;

    private String nombre;

    @ManyToMany(mappedBy = "genero")
    private List<Pelicula> peliculas;
}
