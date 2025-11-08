package com.moviestats.controllers;

import com.moviestats.service.TMDBSyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador REST para sincronización con TMDB.
 * Proporciona endpoints para importar películas desde The Movie Database.
 */
@RestController
@RequestMapping("/api/v1/tmdb")
public class TMDBController {
    
    @Autowired
    private TMDBSyncService tmdbSyncService;
    
    /**
     * Importa películas populares desde TMDB.
     * 
     * @param cantidad Número de películas a importar (por defecto 20, máximo 100)
     * @return Respuesta con el número de películas importadas
     * 
     * Ejemplo: POST /api/v1/tmdb/importar-populares?cantidad=50
     */
    @PostMapping(value = "/importar-populares", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> importPopularMovies(
            @RequestParam(value = "cantidad", required = false, defaultValue = "20") int cantidad) {
        
        // Limitar cantidad máxima para evitar abusos
        if (cantidad > 100) {
            cantidad = 100;
        }
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            int importadas = tmdbSyncService.importPopularMovies(cantidad);
            
            response.put("success", true);
            response.put("mensaje", "Películas populares importadas exitosamente");
            response.put("peliculasImportadas", importadas);
            response.put("peliculasSolicitadas", cantidad);
            
            return new ResponseEntity<>(response, HttpStatus.OK);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("mensaje", "Error al importar películas: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Importa películas mejor valoradas desde TMDB.
     * 
     * @param cantidad Número de películas a importar (por defecto 20, máximo 100)
     * @return Respuesta con el número de películas importadas
     * 
     * Ejemplo: POST /api/v1/tmdb/importar-top-rated?cantidad=30
     */
    @PostMapping(value = "/importar-top-rated", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> importTopRatedMovies(
            @RequestParam(value = "cantidad", required = false, defaultValue = "20") int cantidad) {
        
        if (cantidad > 100) {
            cantidad = 100;
        }
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            int importadas = tmdbSyncService.importTopRatedMovies(cantidad);
            
            response.put("success", true);
            response.put("mensaje", "Películas mejor valoradas importadas exitosamente");
            response.put("peliculasImportadas", importadas);
            response.put("peliculasSolicitadas", cantidad);
            
            return new ResponseEntity<>(response, HttpStatus.OK);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("mensaje", "Error al importar películas: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Busca e importa películas por término de búsqueda.
     * 
     * @param query Término de búsqueda
     * @param cantidad Máximo de películas a importar (por defecto 10, máximo 20)
     * @return Respuesta con el número de películas importadas
     * 
     * Ejemplo: POST /api/v1/tmdb/buscar-importar?query=batman&cantidad=5
     */
    @PostMapping(value = "/buscar-importar", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> searchAndImportMovies(
            @RequestParam("query") String query,
            @RequestParam(value = "cantidad", required = false, defaultValue = "10") int cantidad) {
        
        if (cantidad > 20) {
            cantidad = 20;
        }
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            int importadas = tmdbSyncService.searchAndImportMovies(query, cantidad);
            
            response.put("success", true);
            response.put("mensaje", "Películas importadas desde búsqueda");
            response.put("query", query);
            response.put("peliculasImportadas", importadas);
            response.put("peliculasSolicitadas", cantidad);
            
            return new ResponseEntity<>(response, HttpStatus.OK);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("mensaje", "Error al buscar e importar películas: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Importa una película específica por su ID de TMDB.
     * 
     * @param tmdbId ID de la película en TMDB
     * @return Respuesta indicando si se importó correctamente
     * 
     * Ejemplo: POST /api/v1/tmdb/importar-id/550 (Fight Club)
     */
    @PostMapping(value = "/importar-id/{tmdbId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> importMovieById(@PathVariable Long tmdbId) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            boolean importada = tmdbSyncService.importSingleMovie(tmdbId);
            
            if (importada) {
                response.put("success", true);
                response.put("mensaje", "Película importada exitosamente");
                response.put("tmdbId", tmdbId);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("success", false);
                response.put("mensaje", "La película ya existe o no se pudo importar");
                response.put("tmdbId", tmdbId);
                return new ResponseEntity<>(response, HttpStatus.CONFLICT);
            }
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("mensaje", "Error al importar película: " + e.getMessage());
            response.put("tmdbId", tmdbId);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Endpoint de información sobre endpoints disponibles.
     * 
     * @return Información de ayuda sobre cómo usar los endpoints
     */
    @GetMapping(value = "/ayuda", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> help() {
        Map<String, Object> response = new HashMap<>();
        
        response.put("mensaje", "Endpoints disponibles para importar películas desde TMDB");
        
        Map<String, String> endpoints = new HashMap<>();
        endpoints.put("POST /api/v1/tmdb/importar-populares?cantidad=50", "Importa las películas más populares");
        endpoints.put("POST /api/v1/tmdb/importar-top-rated?cantidad=30", "Importa las películas mejor valoradas");
        endpoints.put("POST /api/v1/tmdb/buscar-importar?query=batman&cantidad=5", "Busca e importa películas por término");
        endpoints.put("POST /api/v1/tmdb/importar-id/550", "Importa una película específica por ID de TMDB");
        
        response.put("endpoints", endpoints);
        response.put("nota", "Las películas duplicadas se ignoran automáticamente");
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
