package com.moviestats.model.business;
import java.util.List;

import com.moviestats.model.Voto;
import com.moviestats.model.business.exceptions.*;

/**
 * Interfaz que define las operaciones de negocio para la gestión de Votos.
 * <p>
 * Incluye métodos para listar, buscar, agregar, actualizar y eliminar Votos,
 * manejando las excepciones correspondientes a reglas de negocio y existencia de datos.
 * </p>
 */

public interface VotoBusiness {
    /**
     * Obtiene la lista completa de Votos.
     *
     * @return Lista de Votos existentes.
     * @throws BusinessException Si ocurre un error general en la lógica de negocio.
     */
    public List<Voto> list() throws BusinessException;

     /**
     * Carga un Voto específico a partir de su identificador único.
     *
     * @param idPelicula Identificador las peliculas a cargar.
     * @return Voto correspondiente al identificador proporcionado.
     * @throws BusinessException Si ocurre un error en la lógica de negocio.
     * @throws NotFoundException  Si no se encuentra un Voto con el identificador dado.
     */
    public Double loadAVG(Long idPelicula) throws BusinessException, NotFoundException;


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

    /**
     * Actualiza los datos de un Voto existente.
     *
     * @param Voto Voto con los datos actualizados.
     * @return Voto actualizado.
     * @throws BusinessException Si ocurre un error en la lógica de negocio.
     * @throws NotFoundException Si el Voto a actualizar no existe.
     * @throws FoundException Si ya existe un Voto igual en el sistema.
     */
    public Voto update(Voto Voto) throws BusinessException, NotFoundException, FoundException;

    /**
     * Elimina un Voto del sistema a partir de su identificador.
     *
     * @param id Identificador del Voto a eliminar.
     * @throws BusinessException Si ocurre un error en la lógica de negocio.
     * @throws NotFoundException Si el Voto a eliminar no existe.
     */
    public void delete(Long id) throws BusinessException, NotFoundException;
}