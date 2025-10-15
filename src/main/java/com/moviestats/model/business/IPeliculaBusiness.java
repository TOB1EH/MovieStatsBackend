package com.moviestats.model.business;
import java.util.List;

import com.moviestats.model.Pelicula;
import com.moviestats.model.business.exceptions.*;

/**
 * Interfaz que define las operaciones de negocio para la gestión de peliculas.
 * <p>
 * Incluye métodos para listar, buscar, agregar, actualizar y eliminar peliculas,
 * manejando las excepciones correspondientes a reglas de negocio y existencia de datos.
 * </p>
 */

public interface IPeliculaBusiness {

    /**
     * Obtiene la lista completa de peliculas.
     *
     * @return Lista de peliculas existentes.
     * @throws BusinessException Si ocurre un error general en la lógica de negocio.
     */
    public List<Pelicula> list() throws BusinessException;

     /**
     * Carga un pelicula específico a partir de su identificador único.
     *
     * @param id Identificador del pelicula a cargar.
     * @return pelicula correspondiente al identificador proporcionado.
     * @throws BusinessException Si ocurre un error en la lógica de negocio.
     * @throws NotFoundException  Si no se encuentra un pelicula con el identificador dado.
     */
    public Pelicula load(Long id) throws BusinessException, NotFoundException;

    /**
     * Carga un pelicula específico a partir de su nombre o descripción.
     *
     * @param pelicula Nombre o descripción del pelicula a cargar.
     * @return pelicula correspondiente al nombre proporcionado.
     * @throws BusinessException Si ocurre un error en la lógica de negocio.
     * @throws NotFoundException  Si no se encuentra un pelicula con el nombre dado.
     */
    public Pelicula load(String pelicula) throws BusinessException, NotFoundException;

    /**
     * Agrega un nuevo pelicula al sistema.
     *
     * @param pelicula pelicula a agregar.
     * @return pelicula agregado, incluyendo su identificador generado.
     * @throws BusinessException Si ocurre un error en la lógica de negocio.
     * @throws FoundException    Si ya existe un pelicula igual en el sistema.
     */
    public Pelicula add(Pelicula pelicula) throws BusinessException, FoundException;

    /**
     * Actualiza los datos de un pelicula existente.
     *
     * @param pelicula pelicula con los datos actualizados.
     * @return pelicula actualizado.
     * @throws BusinessException Si ocurre un error en la lógica de negocio.
     * @throws NotFoundException Si el pelicula a actualizar no existe.
     * @throws FoundException Si ya existe un pelicula igual en el sistema.
     */
    public Pelicula update(Pelicula pelicula) throws BusinessException, NotFoundException, FoundException;

    /**
     * Elimina un pelicula del sistema a partir de su identificador.
     *
     * @param id Identificador del pelicula a eliminar.
     * @throws BusinessException Si ocurre un error en la lógica de negocio.
     * @throws NotFoundException Si el pelicula a eliminar no existe.
     */
    public void delete(Long id) throws BusinessException, NotFoundException;


}
