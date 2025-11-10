package com.moviestats.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para recibir una solicitud de votación desde el frontend.
 * Contiene los IDs de usuario y película en lugar de objetos completos.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VotoRequest {
    /**
     * ID del usuario que emite el voto
     */
    private Long idUsuario;
    
    /**
     * ID de la película a votar
     */
    private Long idPelicula;
    
    /**
     * Puntuación asignada (típicamente 1-10)
     */
    private Integer puntuacion;
}
