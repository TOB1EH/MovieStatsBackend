package com.moviestats.dto.tmdb;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * DTO para mapear la respuesta paginada de películas de TMDB.
 */
public class TMDBMoviePageDTO {
    
    private Integer page;
    
    private List<TMDBMovieDTO> results;
    
    @JsonProperty("total_pages")
    private Integer totalPages;
    
    @JsonProperty("total_results")
    private Integer totalResults;
    
    public Integer getPage() {
        return page;
    }
    
    public void setPage(Integer page) {
        this.page = page;
    }
    
    public List<TMDBMovieDTO> getResults() {
        return results;
    }
    
    public void setResults(List<TMDBMovieDTO> results) {
        this.results = results;
    }
    
    public Integer getTotalPages() {
        return totalPages;
    }
    
    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }
    
    public Integer getTotalResults() {
        return totalResults;
    }
    
    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }
}
