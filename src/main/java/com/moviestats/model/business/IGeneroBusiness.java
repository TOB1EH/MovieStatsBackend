package com.moviestats.model.business;
import java.util.List;

import com.moviestats.model.Genero;
import com.moviestats.model.business.exceptions.*;

/**
 * Interfaz que define las operaciones de negocio para la gestión de generos.
 * <p>
 * Incluye métodos para listar, buscar, agregar, actualizar y eliminar generos,
 * manejando las excepciones correspondientes a reglas de negocio y existencia de datos.
 * </p>
 */

public interface IGeneroBusiness {
    /**
     * Obtiene la lista completa de generos.
     *
     * @return Lista de generos existentes.
     * @throws BusinessException Si ocurre un error general en la lógica de negocio.
     */
    public List<Genero> list() throws BusinessException;

     /**
     * Carga un genero específico a partir de su identificador único.
     *
     * @param id Identificador del genero a cargar.
     * @return genero correspondiente al identificador proporcionado.
     * @throws BusinessException Si ocurre un error en la lógica de negocio.
     * @throws NotFoundException  Si no se encuentra un genero con el identificador dado.
     */
    public Genero load(Long id) throws BusinessException, NotFoundException;

    /**
     * Carga un genero específico a partir de su nombre o descripción.
     *
     * @param genero Nombre o descripción del genero a cargar.
     * @return genero correspondiente al nombre proporcionado.
     * @throws BusinessException Si ocurre un error en la lógica de negocio.
     * @throws NotFoundException  Si no se encuentra un genero con el nombre dado.
     */
    public Genero load(String genero) throws BusinessException, NotFoundException;

    /**
     * Agrega un nuevo genero al sistema.
     *
     * @param genero genero a agregar.
     * @return genero agregado, incluyendo su identificador generado.
     * @throws BusinessException Si ocurre un error en la lógica de negocio.
     * @throws FoundException    Si ya existe un genero igual en el sistema.
     */
    public Genero add(Genero genero) throws BusinessException, FoundException;

    /**
     * Actualiza los datos de un genero existente.
     *
     * @param genero genero con los datos actualizados.
     * @return genero actualizado.
     * @throws BusinessException Si ocurre un error en la lógica de negocio.
     * @throws NotFoundException Si el genero a actualizar no existe.
     * @throws FoundException Si ya existe un genero igual en el sistema.
     */
    public Genero update(Genero genero) throws BusinessException, NotFoundException, FoundException;

    /**
     * Elimina un genero del sistema a partir de su identificador.
     *
     * @param id Identificador del genero a eliminar.
     * @throws BusinessException Si ocurre un error en la lógica de negocio.
     * @throws NotFoundException Si el genero a eliminar no existe.
     */
    public void delete(Long id) throws BusinessException, NotFoundException;
}