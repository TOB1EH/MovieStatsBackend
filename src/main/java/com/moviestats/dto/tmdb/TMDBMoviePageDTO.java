package com.moviestats.dto.tmdb;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

/**
 * DTO para mapear la respuesta paginada de películas de TMDB.
 */
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class TMDBMoviePageDTO {

    private Integer page;

    private List<TMDBMovieDTO> results;

    @JsonProperty("total_pages")
    private Integer totalPages;

    @JsonProperty("total_results")
    private Integer totalResults;
}
