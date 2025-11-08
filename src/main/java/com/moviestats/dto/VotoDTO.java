package com.moviestats.dto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class VotoDTO {
    private Long idVoto;
    private Integer numero_voto;

    public VotoDTO(Long idVoto, Integer numero_voto) {
        this.idVoto = idVoto;
        this.numero_voto = numero_voto;
    }
}
