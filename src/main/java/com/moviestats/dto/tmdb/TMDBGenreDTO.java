package com.moviestats.dto.tmdb;

/**
 * DTO para representar un género de TMDB.
 */
public class TMDBGenreDTO {
    
    private Integer id;
    private String name;
    
    public TMDBGenreDTO() {
    }
    
    public TMDBGenreDTO(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
}
