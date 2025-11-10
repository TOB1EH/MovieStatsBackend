package com.moviestats.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moviestats.dto.VotoRequest;
import com.moviestats.model.Pelicula;
import com.moviestats.model.Usuario;
import com.moviestats.model.Voto;
import com.moviestats.model.business.exceptions.*;
import com.moviestats.model.persistence.PeliculaRepository;
import com.moviestats.model.persistence.UsuarioRepository;
import com.moviestats.util.IStandardResponseBusiness;
import com.moviestats.model.business.IVotoBusiness;


/**
 * Controlador REST para la gestión de votos.
 * <p>
 * Proporciona endpoints para listar y agregar votos mediante solicitudes HTTP.
 * Utiliza las rutas definidas en {@link Constants#URL_votoS}.
 * </p>
 */
@RestController
@RequestMapping(Constants.URL_VOTOS)
public class VotoRestController {

    /**
     * Componente de negocio encargado de construir respuestas estándar.
     */
    @Autowired
    private IStandardResponseBusiness response;

    /**
     * Componente de negocio encargado de la lógica de votos.
     */
    @Autowired
    private IVotoBusiness votoBusiness;
    
    /**
     * Repositorio para acceder a la información de usuarios.
     */
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    /**
     * Repositorio para acceder a la información de películas.
     */
    @Autowired
    private PeliculaRepository peliculaRepository;

    /**
     * Endpoint para agregar un nuevo voto.
     * <p>
     * Responde a solicitudes HTTP POST con un objeto {@link VotoRequest} en el cuerpo de la solicitud.
     * Devuelve la ubicación del nuevo recurso en el encabezado HTTP 'Location'.
     * </p>
     *
     * @param votoRequest El DTO con idUsuario, idPelicula y puntuacion.
     * @return ResponseEntity que indica el resultado de la operación.
     *         - {@link HttpStatus#CREATED} si el voto se creó correctamente.
     *         - {@link HttpStatus#FOUND} si ya existe un voto similar ({@link FoundException}).
     *         - {@link HttpStatus#NOT_FOUND} si el usuario o película no existen.
     *         - {@link HttpStatus#INTERNAL_SERVER_ERROR} si ocurre un {@link BusinessException}.
     */
    @PostMapping(value = "/numero")
    public ResponseEntity<?> add(@RequestBody VotoRequest votoRequest) {
        try {
            // Validar que los IDs no sean nulos
            if (votoRequest.getIdUsuario() == null || votoRequest.getIdPelicula() == null || votoRequest.getPuntuacion() == null) {
                return new ResponseEntity<>(response.build(HttpStatus.BAD_REQUEST, null, "IDs de usuario, película y puntuación son requeridos"),
                    HttpStatus.BAD_REQUEST);
            }
            
            // Buscar el usuario por ID
            Usuario usuario = usuarioRepository.findById(votoRequest.getIdUsuario())
                .orElseThrow(() -> new NotFoundException("Usuario con ID " + votoRequest.getIdUsuario() + " no encontrado"));
            
            // Buscar la película por ID
            Pelicula pelicula = peliculaRepository.findById(votoRequest.getIdPelicula())
                .orElseThrow(() -> new NotFoundException("Película con ID " + votoRequest.getIdPelicula() + " no encontrada"));
            
            // Crear el objeto Voto con las relaciones completas
            Voto voto = new Voto();
            voto.setUsuario(usuario);
            voto.setPelicula(pelicula);
            voto.setNumero_voto(votoRequest.getPuntuacion());
            
            // Guardar el voto a través del servicio de negocio
            Voto savedVoto = votoBusiness.add(voto);
            
            // Preparar respuesta con header Location
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("location", Constants.URL_VOTOS + "/" + savedVoto.getIdVoto());
            
            return new ResponseEntity<>(responseHeaders, HttpStatus.CREATED);
            
        } catch(NotFoundException e) {
            return new ResponseEntity<>(response.build(HttpStatus.NOT_FOUND, e, e.getMessage()),
                HttpStatus.NOT_FOUND);
        } catch(BusinessException e) {
            return new ResponseEntity<>(response.build(HttpStatus.INTERNAL_SERVER_ERROR, e, e.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR);
        } catch(FoundException e) {
            return new ResponseEntity<>(response.build(HttpStatus.FOUND, e, e.getMessage()), 
                HttpStatus.FOUND);
        }
    }

    @GetMapping(value = "/id-pelicula/{idPelicula}/id-usuario/{idUsuario}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> load(@PathVariable Long idPelicula, @PathVariable Long idUsuario) {
        try {
            return new ResponseEntity<>(votoBusiness.loadUsuarioDTO(idUsuario, idPelicula), HttpStatus.OK);
        } catch(BusinessException e) {
            return new ResponseEntity<>(response.build(HttpStatus.INTERNAL_SERVER_ERROR, e, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch(NotFoundException e) {
            return new ResponseEntity<>(response.build(HttpStatus.NOT_FOUND, e, e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }
    
}
