package com.moviestats.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Respuesta paginada para películas.
 */
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class PeliculaPageDTO {
    private List<PeliculaDTO> data;
    private long total;
}
