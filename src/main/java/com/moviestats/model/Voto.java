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

@Entity
@Table(name = "VOTES")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Voto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idVoto;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Pelicula pelicula;
    
    private int numero_voto;
}
