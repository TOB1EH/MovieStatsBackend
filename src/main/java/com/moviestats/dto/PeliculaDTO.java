package com.moviestats.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO simple para exponer datos de película al frontend.
 */
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class PeliculaDTO {

    private long id;
    private String title;
    private Integer year;
    private Float rating;
    private String poster;
    private String overview;
    private List<String> genres;
}
