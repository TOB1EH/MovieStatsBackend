package com.moviestats.dto.tmdb;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * DTO para mapear la respuesta de una película de TMDB API.
 * Contiene todos los campos relevantes de una película obtenida desde TMDB.
 */
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
    
    // Getters y Setters
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getOverview() {
        return overview;
    }
    
    public void setOverview(String overview) {
        this.overview = overview;
    }
    
    public String getPosterPath() {
        return posterPath;
    }
    
    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }
    
    public String getBackdropPath() {
        return backdropPath;
    }
    
    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }
    
    public String getReleaseDate() {
        return releaseDate;
    }
    
    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
    
    public Float getVoteAverage() {
        return voteAverage;
    }
    
    public void setVoteAverage(Float voteAverage) {
        this.voteAverage = voteAverage;
    }
    
    public Integer getVoteCount() {
        return voteCount;
    }
    
    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }
    
    public String getOriginalLanguage() {
        return originalLanguage;
    }
    
    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }
    
    public String getOriginalTitle() {
        return originalTitle;
    }
    
    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }
    
    public List<Integer> getGenreIds() {
        return genreIds;
    }
    
    public void setGenreIds(List<Integer> genreIds) {
        this.genreIds = genreIds;
    }
    
    public List<TMDBGenreDTO> getGenres() {
        return genres;
    }
    
    public void setGenres(List<TMDBGenreDTO> genres) {
        this.genres = genres;
    }
    
    public Integer getRuntime() {
        return runtime;
    }
    
    public void setRuntime(Integer runtime) {
        this.runtime = runtime;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getTagline() {
        return tagline;
    }
    
    public void setTagline(String tagline) {
        this.tagline = tagline;
    }
    
    public Boolean getAdult() {
        return adult;
    }
    
    public void setAdult(Boolean adult) {
        this.adult = adult;
    }
    
    public Double getPopularity() {
        return popularity;
    }
    
    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }
}
