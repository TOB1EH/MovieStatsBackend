package com.moviestats.dto;

import java.util.List;

/**
 * Respuesta paginada para películas.
 */
public class PeliculaPageDTO {
    private List<PeliculaDTO> data;
    private long total;

    public PeliculaPageDTO() {}

    public PeliculaPageDTO(List<PeliculaDTO> data, long total) {
        this.data = data;
        this.total = total;
    }

    public List<PeliculaDTO> getData() {
        return data;
    }

    public void setData(List<PeliculaDTO> data) {
        this.data = data;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
