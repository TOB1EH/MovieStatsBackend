package com.moviestats.model.business;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moviestats.model.Pelicula;
import com.moviestats.model.Voto;
import com.moviestats.model.business.exceptions.*;
import com.moviestats.model.persistence.PeliculaRepository;
import com.moviestats.model.persistence.VotoRepository;

import java.util.Optional;

import lombok.extern.slf4j.Slf4j;

/**
 * Implementación de la interfaz {@link IPeliculaBusiness} que gestiona la lógica 
 * de negocio para los peliculas.
 * <p>
 * Esta clase utiliza {@link PeliculaRepository} para acceder a los datos persistentes 
 * y aplica la lógica de negocio correspondiente. Los métodos lanzan excepciones
 * específicas para manejar errores de negocio, recursos no encontrados o duplicados.
 * </p>
 *
 * <p>
 * Anotaciones:
 * <ul>
 *   <li>{@code @Service}: Marca la clase como un componente de servicio de Spring.</li>
 *   <li>{@code @Slf4j}: Proporciona un logger para registrar errores y eventos importantes.</li>
 * </ul>
 * </p>
 */
@Service
@Slf4j
public class VotoBusiness {
    
     /**
     * Repositorio para acceder a los datos de peliculas.
     * <p>
     * Inyectado automáticamente por Spring.
     * </p>
     */
    @Autowired
    private VotoRepository votoDAO;

    private PeliculaBusiness peliculaBusiness;

     /**
     * Carga un Voto específico a partir de su identificador único.
     *
     * @param idPelicula Identificador las peliculas a cargar.
     * @return Voto correspondiente al identificador proporcionado.
     * @throws BusinessException Si ocurre un error en la lógica de negocio.
     * @throws NotFoundException  Si no se encuentra un Voto con el identificador dado.
     */
    public Double loadAVG(Long idPelicula) throws BusinessException, NotFoundException{
        Double promedio;

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


     /**
     * Carga un Voto específico a partir de su identificador único.
     *
     * @param idPelicula Identificador las peliculas a cargar.
     * @return Voto correspondiente al identificador proporcionado.
     * @throws BusinessException Si ocurre un error en la lógica de negocio.
     * @throws NotFoundException  Si no se encuentra un Voto con el identificador dado.
     */
    public Long loadCOUNT(Long idPelicula) throws BusinessException, NotFoundException{
        Long promedio;

        try {
            promedio = votoDAO.obtenerCantidadDeVotosPorPelicula(idPelicula);
        } catch(Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
        if(promedio == null) {
            throw NotFoundException.builder().message("No se encuentran los votos de pelicula con id: " + idPelicula).build();
        }
        return promedio;
    }


         /**
     * Carga un Voto específico a partir de su identificador único.
     *
     * @param idVoto Identificador las peliculas a cargar.
     * @return Voto correspondiente al identificador proporcionado.
     * @throws BusinessException Si ocurre un error en la lógica de negocio.
     * @throws NotFoundException  Si no se encuentra un Voto con el identificador dado.
     */
    public Voto load(Long id) throws BusinessException, NotFoundException {
        Optional<Voto> r;

        try {
            r = votoDAO.findById(id);
        } catch(Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
        if(r.isEmpty()) {
            throw NotFoundException.builder().message("No se encuentra el pelicula con id: " + id).build();
        }
        return r.get();
    }

    /**
     * Carga 
     *
     * @param Voto Nombre o descripción del Voto a cargar.
     * @return Voto correspondiente al nombre proporcionado.
     * @throws BusinessException Si ocurre un error en la lógica de negocio.
     * @throws NotFoundException  Si no se encuentra un Voto con el nombre dado.
     */
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

    /**
     * Agrega un nuevo Voto al sistema.

     * @param Voto Voto a agregar.
     * @return Voto agregado, incluyendo su identificador generado.
     * @throws BusinessException Si ocurre un error en la lógica de negocio.
     * @throws FoundException    Si ya existe un Voto igual en el sistema.
     */
    public Voto add(Voto voto) throws BusinessException, FoundException{
        try {
            Pelicula pelicula = peliculaBusiness.load(voto.getPelicula().getIdPelicula());
            pelicula.setPuntuacion(loadAVG(voto.getPelicula().getIdPelicula()));
            pelicula.setVotos(loadCOUNT(voto.getPelicula().getIdPelicula()));
            Voto votoaux = loadUsuario(voto.getPelicula().getIdPelicula(),voto.getUsuario().getIdUsuario());
            if(votoaux != null){
                votoaux.setNumero_voto(voto.getNumero_voto());

                return votoDAO.save(votoaux);
            }
        } catch (NotFoundException e) {
        }
        
        try {
            return votoDAO.save(voto);
        } catch(Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
    }

}