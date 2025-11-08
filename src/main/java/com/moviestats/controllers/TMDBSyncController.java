package com.moviestats.controllers;

import com.moviestats.service.TMDBAutoSyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador REST para sincronización masiva automática con TMDB.
 * Proporciona endpoints para importar grandes cantidades de películas en segundo plano.
 */
@RestController
@RequestMapping("/api/v1/tmdb/sync")
public class TMDBSyncController {
    
    @Autowired
    private TMDBAutoSyncService autoSyncService;
    
    /**
     * Sincronización rápida: 200 películas (ejecuta en segundo plano).
     * 
     * @return Respuesta inmediata indicando que la sincronización comenzó
     * 
     * Ejemplo: POST /api/v1/tmdb/sync/quick
     */
    @PostMapping(value = "/quick", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> quickSync() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            autoSyncService.quickSync();
            
            response.put("success", true);
            response.put("mensaje", "Sincronización rápida iniciada en segundo plano");
            response.put("cantidad", 200);
            response.put("nota", "Usa GET /api/v1/tmdb/sync/status para ver el progreso");
            
            return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("mensaje", "Error al iniciar sincronización: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Sincronización completa: 1000 películas (ejecuta en segundo plano).
     * 
     * @return Respuesta inmediata indicando que la sincronización comenzó
     * 
     * Ejemplo: POST /api/v1/tmdb/sync/full
     */
    @PostMapping(value = "/full", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> fullSync() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            autoSyncService.fullSync();
            
            response.put("success", true);
            response.put("mensaje", "Sincronización completa iniciada en segundo plano");
            response.put("cantidad", 1000);
            response.put("estimado", "~10-15 minutos");
            response.put("nota", "Usa GET /api/v1/tmdb/sync/status para ver el progreso");
            
            return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("mensaje", "Error al iniciar sincronización: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Sincronización personalizada con cantidad específica.
     * 
     * @param cantidad Número de películas a importar
     * @return Respuesta inmediata indicando que la sincronización comenzó
     * 
     * Ejemplo: POST /api/v1/tmdb/sync/custom?cantidad=500
     */
    @PostMapping(value = "/custom", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> customSync(@RequestParam("cantidad") int cantidad) {
        Map<String, Object> response = new HashMap<>();
        
        // Limitar a máximo 2000 películas por seguridad
        if (cantidad > 2000) {
            cantidad = 2000;
        }
        
        if (cantidad < 10) {
            cantidad = 10;
        }
        
        try {
            autoSyncService.syncMoviesAsync(cantidad);
            
            int minutosEstimados = (int) Math.ceil(cantidad / 20.0); // ~20 películas por minuto
            
            response.put("success", true);
            response.put("mensaje", "Sincronización personalizada iniciada en segundo plano");
            response.put("cantidad", cantidad);
            response.put("estimado", minutosEstimados + " minutos aproximadamente");
            response.put("nota", "Usa GET /api/v1/tmdb/sync/status para ver el progreso");
            
            return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("mensaje", "Error al iniciar sincronización: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Sincronización por categorías: importa películas variadas de diferentes géneros.
     * 
     * @param cantidadPorCategoria Películas a importar por cada categoría (default: 20)
     * @return Respuesta inmediata indicando que la sincronización comenzó
     * 
     * Ejemplo: POST /api/v1/tmdb/sync/categories?cantidad=20
     */
    @PostMapping(value = "/categories", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> syncByCategories(
            @RequestParam(value = "cantidad", required = false, defaultValue = "20") int cantidadPorCategoria) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            autoSyncService.syncByCategories(cantidadPorCategoria);
            
            response.put("success", true);
            response.put("mensaje", "Sincronización por categorías iniciada");
            response.put("categorias", "Acción, Ciencia Ficción, Drama, Comedia, Terror");
            response.put("peliculasPorCategoria", cantidadPorCategoria);
            response.put("totalAproximado", cantidadPorCategoria * 5);
            response.put("nota", "Usa GET /api/v1/tmdb/sync/status para ver el progreso");
            
            return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("mensaje", "Error al iniciar sincronización: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Consulta el estado actual de la sincronización.
     * 
     * @return Estado de la sincronización (en progreso, películas importadas, etc.)
     * 
     * Ejemplo: GET /api/v1/tmdb/sync/status
     */
    @GetMapping(value = "/status", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getSyncStatus() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            TMDBAutoSyncService.SyncStatus status = autoSyncService.getStatus();
            
            response.put("enProgreso", status.isInProgress());
            response.put("progresoActual", status.getCurrentProgress());
            response.put("totalImportado", status.getTotalImported());
            
            if (status.isInProgress()) {
                response.put("mensaje", "Sincronización en progreso...");
            } else {
                response.put("mensaje", "No hay sincronización activa");
            }
            
            return new ResponseEntity<>(response, HttpStatus.OK);
            
        } catch (Exception e) {
            response.put("error", "Error al consultar estado: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Endpoint de ayuda sobre sincronización masiva.
     * 
     * @return Información sobre los endpoints disponibles
     */
    @GetMapping(value = "/ayuda", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> help() {
        Map<String, Object> response = new HashMap<>();
        
        response.put("mensaje", "Endpoints de sincronización masiva con TMDB");
        
        Map<String, String> endpoints = new HashMap<>();
        endpoints.put("POST /api/v1/tmdb/sync/quick", "Sincronización rápida (200 películas, ~3 min)");
        endpoints.put("POST /api/v1/tmdb/sync/full", "Sincronización completa (1000 películas, ~15 min)");
        endpoints.put("POST /api/v1/tmdb/sync/custom?cantidad=500", "Sincronización personalizada");
        endpoints.put("POST /api/v1/tmdb/sync/categories?cantidad=20", "Por categorías (Acción, Sci-Fi, etc.)");
        endpoints.put("GET /api/v1/tmdb/sync/status", "Consultar estado de sincronización");
        
        response.put("endpoints", endpoints);
        response.put("nota", "Todas las sincronizaciones se ejecutan en segundo plano");
        response.put("importante", "Las sincronizaciones ignoran películas duplicadas automáticamente");
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
