package com.moviestats.dto.tmdb;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO para representar un género de TMDB.
 */
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class TMDBGenreDTO {

    private Integer id;
    private String name;
}
