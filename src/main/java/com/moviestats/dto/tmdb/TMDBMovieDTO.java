package com.moviestats.dto.tmdb;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * DTO para mapear la respuesta de una película de TMDB API.
 * Contiene todos los campos relevantes de una película obtenida desde TMDB.
 */
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class TMDBMovieDTO {
    
    private Long id;
    private String title;
    private String overview;
    
    @JsonProperty("poster_path")
    private String posterPath;
    
    @JsonProperty("backdrop_path")
    private String backdropPath;
    
    @JsonProperty("release_date")
    private String releaseDate;
    
    @JsonProperty("vote_average")
    private Float voteAverage;
    
    @JsonProperty("vote_count")
    private Integer voteCount;
    
    @JsonProperty("original_language")
    private String originalLanguage;
    
    @JsonProperty("original_title")
    private String originalTitle;
    
    @JsonProperty("genre_ids")
    private List<Integer> genreIds;
    
    private List<TMDBGenreDTO> genres;
    
    private Integer runtime;
    
    private String status;
    
    private String tagline;
    
    private Boolean adult;
    
    private Double popularity;
}
