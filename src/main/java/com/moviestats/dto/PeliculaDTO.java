package com.moviestats.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO simple usado por el frontend AdminView:
 * - id, title, year, genre, active
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PeliculaDTO {
    private Long id;
    private String title;
    private Integer year;
    private String genre;
}