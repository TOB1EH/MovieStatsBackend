package com.moviestats.model.business;

import com.moviestats.dto.VotoDTO;
import com.moviestats.model.Voto;
import com.moviestats.model.business.exceptions.*;

/**
 * Interfaz que define las operaciones de negocio para la gestión de Votos.
 * <p>
 * Incluye métodos para listar, buscar, agregar, actualizar y eliminar Votos,
 * manejando las excepciones correspondientes a reglas de negocio y existencia de datos.
 * </p>
 */

public interface IVotoBusiness {

     /**
     * Carga un Voto específico a partir de su identificador único.
     *
     * @param idPelicula Identificador las peliculas a cargar.
     * @return Voto correspondiente al identificador proporcionado.
     * @throws BusinessException Si ocurre un error en la lógica de negocio.
     * @throws NotFoundException  Si no se encuentra un Voto con el identificador dado.
     */
    public Float loadAVG(Long idPelicula) throws BusinessException, NotFoundException;


         /**
     * Carga un Voto específico a partir de su identificador único.
     *
     * @param idPelicula Identificador las peliculas a cargar.
     * @return Voto correspondiente al identificador proporcionado.
     * @throws BusinessException Si ocurre un error en la lógica de negocio.
     * @throws NotFoundException  Si no se encuentra un Voto con el identificador dado.
     */
    public Voto load(Long id) throws BusinessException, NotFoundException;

    /**
     * Carga 
     *
     * @param Voto Nombre o descripción del Voto a cargar.
     * @return Voto correspondiente al nombre proporcionado.
     * @throws BusinessException Si ocurre un error en la lógica de negocio.
     * @throws NotFoundException  Si no se encuentra un Voto con el nombre dado.
     */
    public Voto loadUsuario(Long idUsuario, Long idPelicula) throws BusinessException, NotFoundException;

    /**
     * Agrega un nuevo Voto al sistema.

     * @param Voto Voto a agregar.
     * @return Voto agregado, incluyendo su identificador generado.
     * @throws BusinessException Si ocurre un error en la lógica de negocio.
     * @throws FoundException    Si ya existe un Voto igual en el sistema.
     */
    public Voto add(Voto voto) throws BusinessException, FoundException;

    public VotoDTO loadUsuarioDTO(Long idUsuario, Long idPelicula) throws BusinessException, NotFoundException;

}