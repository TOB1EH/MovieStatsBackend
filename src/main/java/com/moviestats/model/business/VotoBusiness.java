package com.moviestats.model.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.moviestats.dto.VotoDTO;
import com.moviestats.model.Pelicula;
import com.moviestats.model.Voto;
import com.moviestats.model.business.exceptions.*;
import com.moviestats.model.persistence.VotoRepository;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class VotoBusiness implements IVotoBusiness{
    
    @Autowired
    private VotoRepository votoDAO;

    // CORRECCIÓN: inyectar el servicio de películas
    @Autowired
    private IPeliculaBusiness peliculaBusiness;

    public Float loadAVG(Long idPelicula) throws BusinessException, NotFoundException{
        Float promedio;

        try {
            promedio = votoDAO.obtenerPromedioPorPeliula(idPelicula);
        } catch(Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
        if(promedio == null) {
            throw NotFoundException.builder().message("No se encuentran los votos de pelicula con id: " + idPelicula).build();
        }
        return promedio;
    }

    public Integer loadCOUNT(Long idPelicula) throws BusinessException, NotFoundException{
        Integer cantidad;

        try {
            cantidad = votoDAO.obtenerCantidadDeVotosPorPelicula(idPelicula);
        } catch(Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
        if(cantidad == null) {
            throw NotFoundException.builder().message("No se encuentran los votos de pelicula con id: " + idPelicula).build();
        }
        return cantidad;
    }

    public Voto load(Long id) throws BusinessException, NotFoundException {
        Optional<Voto> r;

        try {
            r = votoDAO.findById(id);
        } catch(Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
        if(r.isEmpty()) {
            throw NotFoundException.builder().message("No se encuentra el voto con id: " + id).build();
        }
        return r.get();
    }

    public Voto loadUsuario(Long idUsuario, Long idPelicula) throws BusinessException, NotFoundException{
        Optional<Voto> r;

        try {
            r = votoDAO.findByIdUsuario(idUsuario, idPelicula);

        } catch(Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
        if(r.isEmpty()) {
            throw NotFoundException.builder().message("No se encuentra el voto del usuario con id: " + idUsuario).build();
        }
        return r.get();
    }

    public VotoDTO loadUsuarioDTO(Long idUsuario, Long idPelicula) throws BusinessException, NotFoundException{
        Optional<VotoDTO> r;

        try {
            r = votoDAO.findByIdUsuarioDTO(idUsuario, idPelicula);

        } catch(Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
        if(r.isEmpty()) {
            throw NotFoundException.builder().message("No se encuentra el voto del usuario con id: " + idUsuario).build();
        }
        return r.get();
    }

    /**
     * Agrega o actualiza un voto. Actualiza también la entidad Pelicula (puntuacion y votos).
     */
    @Override
    @Transactional
    public Voto add(Voto voto) throws BusinessException, FoundException{
        try {
            // carga la película (verifica existencia)
            Pelicula pelicula = peliculaBusiness.load(voto.getPelicula().getIdPelicula());

            // Intento de actualizar avg y count (si hay votos)
            try {
                Float avg = loadAVG(voto.getPelicula().getIdPelicula());
                Integer cnt = loadCOUNT(voto.getPelicula().getIdPelicula());
                pelicula.setPuntuacion(avg);
                pelicula.setVotos(cnt);
            } catch (NotFoundException e) {
                // si no hay votos todavía, los dejamos en null/0 según tu modelo
                pelicula.setPuntuacion(null);
                pelicula.setVotos(0);
            }

            // Persistir la película con los valores actualizados (para que la tabla MOVIES refleje los nuevos valores)
            try {
                peliculaBusiness.update(pelicula);
            } catch (FoundException | BusinessException e) {
                // No esperamos FoundException al actualizar el mismo registro,
                // pero si ocurre lo logueamos y seguimos (no bloqueamos la creación del voto)
                log.warn("No pude actualizar la película tras calcular avg/count: " + e.getMessage());
            }

            // CORRECCIÓN: orden correcto (idUsuario, idPelicula)
            try {
                Voto votoaux = loadUsuario(voto.getUsuario().getIdUsuario(), voto.getPelicula().getIdPelicula());
                // existe voto del usuario para esa peli -> actualizar
                if(votoaux != null){
                    votoaux.setNumero_voto(voto.getNumero_voto());
                    Voto saved = votoDAO.save(votoaux);

                    // después de actualizar voto, recalcular y persistir película nuevamente
                    try {
                        pelicula.setPuntuacion(loadAVG(voto.getPelicula().getIdPelicula()));
                        pelicula.setVotos(loadCOUNT(voto.getPelicula().getIdPelicula()));
                        peliculaBusiness.update(pelicula);
                    } catch(Exception ex) {
                        log.warn("No pude actualizar la película después de actualizar el voto: " + ex.getMessage());
                    }

                    return saved;
                }
            } catch (NotFoundException nf) {
                // no existe voto del usuario -> continuamos para insertar uno nuevo
            }

        } catch (NotFoundException e) {
            // si la película no existe, seguimos para intentar crear el voto (luego fallará por FK / NotFound)
        }
        
        try {
            Voto created = votoDAO.save(voto);

            // después de insertar, recalcular avg/count y persistir película
            try {
                Pelicula pelicula = peliculaBusiness.load(voto.getPelicula().getIdPelicula());
                pelicula.setPuntuacion(loadAVG(voto.getPelicula().getIdPelicula()));
                pelicula.setVotos(loadCOUNT(voto.getPelicula().getIdPelicula()));
                peliculaBusiness.update(pelicula);
            } catch(Exception e) {
                log.warn("No pude actualizar la película tras insertar voto: " + e.getMessage());
            }

            return created;
        } catch(Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
    }

}