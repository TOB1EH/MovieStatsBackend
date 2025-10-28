package com.moviestats.util;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;

import com.moviestats.dto.PeliculaDTO;
import com.moviestats.model.Genero;
import com.moviestats.model.Pelicula;

/**
 * Conversor entre entidad Pelicula y PeliculaDTO (frontend).
 */
public final class PeliculaMapper {

    private PeliculaMapper() {}

    public static PeliculaDTO toDto(Pelicula p) {
        if (p == null) return null;
        PeliculaDTO dto = new PeliculaDTO();
        dto.setId(p.getIdPelicula());
        dto.setTitle(p.getNombre());
        if (p.getFechaSalida() != null) {
            LocalDate ld = p.getFechaSalida().toLocalDate();
            dto.setYear(ld.getYear());
        } else {
            dto.setYear(null);
        }
        if (p.getGenero() != null && !p.getGenero().isEmpty()) {
            Genero g = p.getGenero().get(0);
            dto.setGenre(g != null ? g.getNombre() : null);
        } else {
            dto.setGenre(null);
        }
        return dto;
    }

    /**
     * Convierte DTO a entidad parcial. No setea la lista de Genero completa:
     * el controller se encarga de resolver/crear el Genero y asignarlo.
     */
    public static Pelicula toEntity(PeliculaDTO dto) {
        if (dto == null) return null;
        Pelicula p = new Pelicula();
        if (dto.getId() != null) p.setIdPelicula(dto.getId());
        p.setNombre(dto.getTitle());
        if (dto.getYear() != null && dto.getYear() > 0) {
            LocalDate ld = LocalDate.of(dto.getYear(), 1, 1);
            p.setFechaSalida(Date.valueOf(ld));
        } else {
            p.setFechaSalida(null);
        }
        // genero list se setea en controller
        p.setGenero(new ArrayList<>());
        return p;
    }
}