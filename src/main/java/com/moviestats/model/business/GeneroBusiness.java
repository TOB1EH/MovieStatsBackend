package com.moviestats.model.business;

import com.moviestats.model.Genero;

import com.moviestats.model.persistence.GeneroRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moviestats.model.business.exceptions.*;
import lombok.extern.slf4j.Slf4j;


/**
 * Implementación de la interfaz {@link IGeneroBusiness} que gestiona la lógica 
 * de negocio para los generos.
 * <p>
 * Esta clase utiliza {@link GeneroRepository} para acceder a los datos persistentes 
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
public class GeneroBusiness implements IGeneroBusiness {

    /**
     * Repositorio para acceder a los datos de generos.
     * <p>
     * Inyectado automáticamente por Spring.
     * </p>
     */
    @Autowired
    private GeneroRepository generoDAO;


    /**
     * Obtiene la lista completa de generos.
     *
     * @return Lista de generos existentes en la base de datos.
     * @throws BusinessException Si ocurre un error en la lógica de negocio o en el acceso a datos.
     */
    @Override
    public List<Genero> list() throws BusinessException {
        try {
            return generoDAO.findAll();
        } catch(Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).message(e.getMessage()).build();
        }
    }

    /**
     * Obtiene un genero por su identificador único.
     *
     * @param id Identificador del genero.
     * @return El {@link genero} correspondiente al id proporcionado.
     * @throws BusinessException Si ocurre un error en la lógica de negocio o en el acceso a datos.
     * @throws NotFoundException Si no se encuentra el genero con el id especificado.
     */
    @Override
    public Genero load(Long id) throws BusinessException, NotFoundException {
        Optional<Genero> r;

        try {
            r = generoDAO.findById(id);
        } catch(Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
        if(r.isEmpty()) {
            throw NotFoundException.builder().message("No se encuentra el genero con id: " + id).build();
        }
        return r.get();
    }

    /**
     * Obtiene un genero por su nombre.
     *
     * @param genero Nombre del genero.
     * @return El {@link genero} correspondiente al nombre proporcionado.
     * @throws BusinessException Si ocurre un error en la lógica de negocio o en el acceso a datos.
     * @throws NotFoundException Si no se encuentra un genero con el nombre especificado.
     */
    @Override
    public Genero load(String genero) throws BusinessException, NotFoundException {
        Optional<Genero> r;

        try {
            r = generoDAO.findByNombre(genero);
        } catch(Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
        if(r.isEmpty()) {
            throw NotFoundException.builder().message("No se encuentra el genero de nombre: " + genero).build();
        }
        return r.get();
    }

    /**
     * Agrega un nuevo genero.
     * <p>
     * Antes de agregar, verifica que no exista otro genero con el mismo id o nombre.
     * </p>
     *
     * @param genero genero a agregar.
     * @return El {@link genero} agregado.
     * @throws BusinessException Si ocurre un error en la lógica de negocio o en el acceso a datos.
     * @throws FoundException Si ya existe un genero con el mismo id o nombre.
     */
    @Override
    public Genero add(Genero genero) throws BusinessException, FoundException {
        try {
            load(genero.getIdGenero());
            throw FoundException.builder().message("Se encontró el genero con id: " + genero.getIdGenero()).build();
        } catch(NotFoundException e) {
        }
        try {
            load(genero.getNombre());
            throw FoundException.builder().message("Se encontró el genero con nombre: " + genero.getNombre()).build();
        } catch(NotFoundException e) {
        }

        try {
            return generoDAO.save(genero);
        } catch(Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
    }

    /**
     * Actualiza un genero existente.
     * <p>
     * Verifica que el genero a actualizar exista y que no haya otro genero con el mismo nombre.
     * </p>
     *
     * @param genero genero con los datos actualizados.
     * @return El {@link genero} actualizado.
     * @throws BusinessException Si ocurre un error en la lógica de negocio o en el acceso a datos.
     * @throws NotFoundException Si no se encuentra el genero a actualizar.
     * @throws FoundException Si ya existe otro genero con el mismo nombre.
     */
    @Override
    public Genero update(Genero genero) throws FoundException, BusinessException, NotFoundException {
        load(genero.getIdGenero());
        Optional<Genero> nombreExistente = null;

        try {
            nombreExistente = generoDAO.findByNombreAndIdNot(genero.getNombre(), genero.getIdGenero());
        } catch(Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
        if(nombreExistente.isPresent()) {
            throw FoundException.builder().message("Ya existe un genero con el nombre: " + genero.getNombre()).build();
        }

        try {
            return generoDAO.save(genero);
        } catch(Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
    }

    /**
     * Elimina un genero por su identificador.
     *
     * @param id Identificador del genero a eliminar.
     * @throws BusinessException Si ocurre un error en la lógica de negocio o en el acceso a datos.
     * @throws NotFoundException Si no se encuentra el genero con el id especificado.
     */
    @Override
    public void delete(Long id) throws BusinessException, NotFoundException {
        load(id);
        try {
            generoDAO.deleteById(id);
        } catch(Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
    }
    
}
