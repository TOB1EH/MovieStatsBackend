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

@Entity
@Table(name = "MOVIE_GENRES")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GeneroPelicula {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    @Column(name = "movie_id")
    private long movie_id;
    
    @Column(name = "genre_id")
    private long genre_id;
}
