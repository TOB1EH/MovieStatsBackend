package com.moviestats.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moviestats.model.Voto;
import com.moviestats.model.business.exceptions.*;

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
     * Endpoint para agregar un nuevo voto.
     * <p>
     * Responde a solicitudes HTTP POST con un objeto {@link voto} en el cuerpo de la solicitud.
     * Devuelve la ubicación del nuevo recurso en el encabezado HTTP 'Location'.
     * </p>
     *
     * @param voto El voto a agregar.
     * @return ResponseEntity que indica el resultado de la operación.
     *         - {@link HttpStatus#CREATED} si el voto se creó correctamente.
     *         - {@link HttpStatus#FOUND} si ya existe un voto similar ({@link FoundException}).
     *         - {@link HttpStatus#INTERNAL_SERVER_ERROR} si ocurre un {@link BusinessException}.
     */
    @PostMapping(value = "")
    public ResponseEntity<?> add(@RequestBody Voto voto) {
        try {
            Voto response = votoBusiness.add(voto);
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("location", Constants.URL_VOTOS + "/" + response.getIdVoto());
            return new ResponseEntity<>(responseHeaders, HttpStatus.CREATED);
        } catch(BusinessException e) {
            return new ResponseEntity<>(response.build(HttpStatus.INTERNAL_SERVER_ERROR, e, e.getMessage()),
             HttpStatus.INTERNAL_SERVER_ERROR);
        } catch(FoundException e) {
            return new ResponseEntity<>(response.build(HttpStatus.FOUND, e, e.getMessage()), HttpStatus.FOUND);
        }
    }
    
}
