package com.moviestats.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Servicio de sincronización automática masiva con TMDB.
 * Permite importar grandes cantidades de películas en segundo plano.
 */
@Service
public class TMDBAutoSyncService {
    
    private static final Logger log = LoggerFactory.getLogger(TMDBAutoSyncService.class);
    
    @Autowired
    private TMDBSyncService tmdbSyncService;
    
    private AtomicBoolean syncInProgress = new AtomicBoolean(false);
    private AtomicInteger totalImported = new AtomicInteger(0);
    private AtomicInteger currentProgress = new AtomicInteger(0);
    
    /**
     * Sincroniza una gran cantidad de películas de forma asíncrona.
     * Combina películas populares y mejor valoradas.
     * 
     * @param cantidadTotal Número total de películas a importar
     * @return ID de la sincronización (para consultar progreso)
     */
    @Async
    public void syncMoviesAsync(int cantidadTotal) {
        if (syncInProgress.get()) {
            log.warn("Ya hay una sincronización en progreso, ignorando nueva solicitud");
            return;
        }
        
        try {
            syncInProgress.set(true);
            totalImported.set(0);
            currentProgress.set(0);
            
            log.info("🚀 Iniciando sincronización masiva de {} películas", cantidadTotal);
            
            // Dividir: 60% populares, 40% top-rated
            int populares = (int) (cantidadTotal * 0.6);
            int topRated = cantidadTotal - populares;
            
            // Importar películas populares en lotes
            log.info("📥 Importando {} películas populares...", populares);
            int importadasPopulares = importarEnLotes(populares, "populares");
            
            // Importar películas mejor valoradas en lotes
            log.info("⭐ Importando {} películas mejor valoradas...", topRated);
            int importadasTopRated = importarEnLotes(topRated, "top-rated");
            
            totalImported.set(importadasPopulares + importadasTopRated);
            
            log.info("✅ Sincronización completada: {} películas importadas de {} solicitadas", 
                    totalImported.get(), cantidadTotal);
            
        } catch (Exception e) {
            log.error("❌ Error en sincronización masiva", e);
        } finally {
            syncInProgress.set(false);
        }
    }
    
    /**
     * Importa películas en lotes pequeños para evitar timeouts.
     * 
     * @param cantidad Cantidad total a importar
     * @param tipo "populares" o "top-rated"
     * @return Número de películas importadas
     */
    private int importarEnLotes(int cantidad, String tipo) {
        int totalImportado = 0;
        int loteTamaño = 20; // Importar de a 20 para no sobrecargar
        int lotes = (int) Math.ceil((double) cantidad / loteTamaño);
        
        for (int i = 0; i < lotes; i++) {
            int cantidadLote = Math.min(loteTamaño, cantidad - totalImportado);
            
            try {
                int importadas;
                if ("populares".equals(tipo)) {
                    importadas = tmdbSyncService.importPopularMovies(cantidadLote);
                } else {
                    importadas = tmdbSyncService.importTopRatedMovies(cantidadLote);
                }
                
                totalImportado += importadas;
                currentProgress.addAndGet(importadas);
                
                log.info("📊 Progreso {}: {}/{} películas ({} importadas en este lote)", 
                        tipo, totalImportado, cantidad, importadas);
                
                // Pausa entre lotes para no saturar la API
                Thread.sleep(1000);
                
            } catch (Exception e) {
                log.error("Error importando lote {} de {}", i + 1, lotes, e);
            }
        }
        
        return totalImportado;
    }
    
    /**
     * Sincronización rápida: importa 200 películas (100 populares + 100 top-rated).
     */
    @Async
    public void quickSync() {
        log.info("⚡ Iniciando sincronización rápida (200 películas)");
        syncMoviesAsync(200);
    }
    
    /**
     * Sincronización completa: importa 1000 películas (600 populares + 400 top-rated).
     */
    @Async
    public void fullSync() {
        log.info("🔄 Iniciando sincronización completa (1000 películas)");
        syncMoviesAsync(1000);
    }
    
    /**
     * Sincronización por categorías: importa películas de géneros específicos.
     * 
     * @param cantidadPorCategoria Películas a importar por cada categoría
     */
    @Async
    public void syncByCategories(int cantidadPorCategoria) {
        if (syncInProgress.get()) {
            log.warn("Ya hay una sincronización en progreso");
            return;
        }
        
        try {
            syncInProgress.set(true);
            totalImported.set(0);
            
            log.info("🎬 Sincronización por categorías: {} películas de cada tipo", cantidadPorCategoria);
            
            // Acción
            int accion = tmdbSyncService.searchAndImportMovies("action hero", cantidadPorCategoria);
            totalImported.addAndGet(accion);
            Thread.sleep(2000);
            
            // Ciencia Ficción
            int scifi = tmdbSyncService.searchAndImportMovies("science fiction space", cantidadPorCategoria);
            totalImported.addAndGet(scifi);
            Thread.sleep(2000);
            
            // Drama
            int drama = tmdbSyncService.searchAndImportMovies("drama award", cantidadPorCategoria);
            totalImported.addAndGet(drama);
            Thread.sleep(2000);
            
            // Comedia
            int comedia = tmdbSyncService.searchAndImportMovies("comedy funny", cantidadPorCategoria);
            totalImported.addAndGet(comedia);
            Thread.sleep(2000);
            
            // Terror
            int terror = tmdbSyncService.searchAndImportMovies("horror scary", cantidadPorCategoria);
            totalImported.addAndGet(terror);
            
            log.info("✅ Sincronización por categorías completada: {} películas importadas", totalImported.get());
            
        } catch (Exception e) {
            log.error("Error en sincronización por categorías", e);
        } finally {
            syncInProgress.set(false);
        }
    }
    
    /**
     * Obtiene el estado actual de la sincronización.
     * 
     * @return Estado de la sincronización
     */
    public SyncStatus getStatus() {
        return new SyncStatus(
            syncInProgress.get(),
            currentProgress.get(),
            totalImported.get()
        );
    }
    
    /**
     * Clase para reportar el estado de la sincronización.
     */
    public static class SyncStatus {
        private final boolean inProgress;
        private final int currentProgress;
        private final int totalImported;
        
        public SyncStatus(boolean inProgress, int currentProgress, int totalImported) {
            this.inProgress = inProgress;
            this.currentProgress = currentProgress;
            this.totalImported = totalImported;
        }
        
        public boolean isInProgress() {
            return inProgress;
        }
        
        public int getCurrentProgress() {
            return currentProgress;
        }
        
        public int getTotalImported() {
            return totalImported;
        }
    }
}
