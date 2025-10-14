package com.moviestats.model.business;

import com.moviestats.model.Pelicula;

import com.moviestats.model.persistence.PeliculaRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moviestats.model.business.exceptions.*;
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
public class PeliculaBusiness implements IPeliculaBusiness {

    /**
     * Repositorio para acceder a los datos de peliculas.
     * <p>
     * Inyectado automáticamente por Spring.
     * </p>
     */
    @Autowired
    private PeliculaRepository peliculaDAO;


    /**
     * Obtiene la lista completa de peliculas.
     *
     * @return Lista de peliculas existentes en la base de datos.
     * @throws BusinessException Si ocurre un error en la lógica de negocio o en el acceso a datos.
     */
    @Override
    public List<Pelicula> list() throws BusinessException {
        try {
            return peliculaDAO.findAll();
        } catch(Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).message(e.getMessage()).build();
        }
    }

    /**
     * Obtiene un pelicula por su identificador único.
     *
     * @param id Identificador del pelicula.
     * @return El {@link pelicula} correspondiente al id proporcionado.
     * @throws BusinessException Si ocurre un error en la lógica de negocio o en el acceso a datos.
     * @throws NotFoundException Si no se encuentra el pelicula con el id especificado.
     */
    @Override
    public Pelicula load(Long id) throws BusinessException, NotFoundException {
        Optional<Pelicula> r;

        try {
            r = peliculaDAO.findById(id);
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
     * Obtiene un pelicula por su nombre.
     *
     * @param pelicula Nombre del pelicula.
     * @return El {@link pelicula} correspondiente al nombre proporcionado.
     * @throws BusinessException Si ocurre un error en la lógica de negocio o en el acceso a datos.
     * @throws NotFoundException Si no se encuentra un pelicula con el nombre especificado.
     */
    @Override
    public Pelicula load(String pelicula) throws BusinessException, NotFoundException {
        Optional<Pelicula> r;

        try {
            r = peliculaDAO.findBypelicula(pelicula);
        } catch(Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
        if(r.isEmpty()) {
            throw NotFoundException.builder().message("No se encuentra el pelicula de nombre: " + pelicula).build();
        }
        return r.get();
    }

    /**
     * Agrega un nuevo pelicula.
     * <p>
     * Antes de agregar, verifica que no exista otro pelicula con el mismo id o nombre.
     * </p>
     *
     * @param pelicula pelicula a agregar.
     * @return El {@link pelicula} agregado.
     * @throws BusinessException Si ocurre un error en la lógica de negocio o en el acceso a datos.
     * @throws FoundException Si ya existe un pelicula con el mismo id o nombre.
     */
    @Override
    public Pelicula add(Pelicula pelicula) throws BusinessException, FoundException {
        try {
            load(pelicula.getIdPelicula());
            throw FoundException.builder().message("Se encontró el pelicula con id: " + pelicula.getIdPelicula()).build();
        } catch(NotFoundException e) {
        }
        try {
            load(pelicula.getNombre());
            throw FoundException.builder().message("Se encontró el pelicula con nombre: " + pelicula.getNombre()).build();
        } catch(NotFoundException e) {
        }

        try {
            return peliculaDAO.save(pelicula);
        } catch(Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
    }

    /**
     * Actualiza un pelicula existente.
     * <p>
     * Verifica que el pelicula a actualizar exista y que no haya otro pelicula con el mismo nombre.
     * </p>
     *
     * @param pelicula pelicula con los datos actualizados.
     * @return El {@link pelicula} actualizado.
     * @throws BusinessException Si ocurre un error en la lógica de negocio o en el acceso a datos.
     * @throws NotFoundException Si no se encuentra el pelicula a actualizar.
     * @throws FoundException Si ya existe otro pelicula con el mismo nombre.
     */
    @Override
    public Pelicula update(Pelicula pelicula) throws FoundException, BusinessException, NotFoundException {
        load(pelicula.getIdPelicula());
        Optional<Pelicula> nombreExistente = null;

        try {
            nombreExistente = peliculaDAO.findBypeliculaAndIdNot(pelicula.getNombre(), pelicula.getIdPelicula());
        } catch(Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
        if(nombreExistente.isPresent()) {
            throw FoundException.builder().message("Ya existe un pelicula con el nombre: " + pelicula.getNombre()).build();
        }

        try {
            return peliculaDAO.save(pelicula);
        } catch(Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
    }

    /**
     * Elimina un pelicula por su identificador.
     *
     * @param id Identificador del pelicula a eliminar.
     * @throws BusinessException Si ocurre un error en la lógica de negocio o en el acceso a datos.
     * @throws NotFoundException Si no se encuentra el pelicula con el id especificado.
     */
    @Override
    public void delete(Long id) throws BusinessException, NotFoundException {
        load(id);
        try {
            peliculaDAO.deleteById(id);
        } catch(Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
    }
    
}

