package com.moviestats.service;

import com.moviestats.config.TMDBConfig;
import com.moviestats.config.TMDBImageConfig;
import com.moviestats.dto.tmdb.TMDBMovieDTO;
import com.moviestats.dto.tmdb.TMDBMoviePageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Servicio para interactuar con TMDB API.
 * Proporciona métodos para obtener películas populares, buscar, obtener detalles, etc.
 */
@Service
public class TMDBService {
    
    private static final Logger log = LoggerFactory.getLogger(TMDBService.class);
    
    @Autowired
    private TMDBConfig tmdbConfig;
    
    @Autowired
    private TMDBImageConfig imageConfig;
    
    private final RestTemplate restTemplate = new RestTemplate();
    
    /**
     * Obtiene películas populares de TMDB.
     * 
     * @param page Número de página (1-based)
     * @return Página con películas populares
     */
    public TMDBMoviePageDTO getPopularMovies(int page) {
        try {
            String url = UriComponentsBuilder
                .fromUriString(tmdbConfig.getBaseUrl() + "/movie/popular")
                .queryParam("api_key", tmdbConfig.getKey())
                .queryParam("language", tmdbConfig.getLanguage())
                .queryParam("page", page)
                .toUriString();
            
            log.info("Obteniendo películas populares de TMDB - Página: {}", page);
            return restTemplate.getForObject(url, TMDBMoviePageDTO.class);
            
        } catch (Exception e) {
            log.error("Error al obtener películas populares de TMDB", e);
            return null;
        }
    }
    
    /**
     * Obtiene los estrenos recientes.
     * 
     * @param page Número de página
     * @return Página con películas recientes
     */
    public TMDBMoviePageDTO getNowPlayingMovies(int page) {
        try {
            String url = UriComponentsBuilder
                .fromUriString(tmdbConfig.getBaseUrl() + "/movie/now_playing")
                .queryParam("api_key", tmdbConfig.getKey())
                .queryParam("language", tmdbConfig.getLanguage())
                .queryParam("page", page)
                .toUriString();
            
            log.info("Obteniendo estrenos recientes de TMDB - Página: {}", page);
            return restTemplate.getForObject(url, TMDBMoviePageDTO.class);
            
        } catch (Exception e) {
            log.error("Error al obtener estrenos recientes de TMDB", e);
            return null;
        }
    }
    
    /**
     * Obtiene películas mejor valoradas.
     * 
     * @param page Número de página
     * @return Página con películas mejor valoradas
     */
    public TMDBMoviePageDTO getTopRatedMovies(int page) {
        try {
            String url = UriComponentsBuilder
                .fromUriString(tmdbConfig.getBaseUrl() + "/movie/top_rated")
                .queryParam("api_key", tmdbConfig.getKey())
                .queryParam("language", tmdbConfig.getLanguage())
                .queryParam("page", page)
                .toUriString();
            
            log.info("Obteniendo películas mejor valoradas de TMDB - Página: {}", page);
            return restTemplate.getForObject(url, TMDBMoviePageDTO.class);
            
        } catch (Exception e) {
            log.error("Error al obtener películas mejor valoradas de TMDB", e);
            return null;
        }
    }
    
    /**
     * Busca películas por query.
     * 
     * @param query Texto de búsqueda
     * @param page Número de página
     * @return Página con resultados de búsqueda
     */
    public TMDBMoviePageDTO searchMovies(String query, int page) {
        try {
            String url = UriComponentsBuilder
                .fromUriString(tmdbConfig.getBaseUrl() + "/search/movie")
                .queryParam("api_key", tmdbConfig.getKey())
                .queryParam("language", tmdbConfig.getLanguage())
                .queryParam("query", query)
                .queryParam("page", page)
                .toUriString();
            
            log.info("Buscando películas en TMDB: {} - Página: {}", query, page);
            return restTemplate.getForObject(url, TMDBMoviePageDTO.class);
            
        } catch (Exception e) {
            log.error("Error al buscar películas en TMDB", e);
            return null;
        }
    }
    
    /**
     * Obtiene detalles completos de una película por su ID de TMDB.
     * 
     * @param tmdbId ID de la película en TMDB
     * @return Detalles completos de la película
     */
    public TMDBMovieDTO getMovieDetails(Long tmdbId) {
        try {
            String url = UriComponentsBuilder
                .fromUriString(tmdbConfig.getBaseUrl() + "/movie/" + tmdbId)
                .queryParam("api_key", tmdbConfig.getKey())
                .queryParam("language", tmdbConfig.getLanguage())
                .toUriString();
            
            log.info("Obteniendo detalles de película TMDB ID: {}", tmdbId);
            return restTemplate.getForObject(url, TMDBMovieDTO.class);
            
        } catch (Exception e) {
            log.error("Error al obtener detalles de película TMDB ID: {}", tmdbId, e);
            return null;
        }
    }
    
    /**
     * Obtiene películas por género.
     * 
     * @param genreId ID del género en TMDB
     * @param page Número de página
     * @return Página con películas del género especificado
     */
    public TMDBMoviePageDTO getMoviesByGenre(Integer genreId, int page) {
        try {
            String url = UriComponentsBuilder
                .fromUriString(tmdbConfig.getBaseUrl() + "/discover/movie")
                .queryParam("api_key", tmdbConfig.getKey())
                .queryParam("language", tmdbConfig.getLanguage())
                .queryParam("with_genres", genreId)
                .queryParam("page", page)
                .queryParam("sort_by", "popularity.desc")
                .toUriString();
            
            log.info("Obteniendo películas por género {} de TMDB - Página: {}", genreId, page);
            return restTemplate.getForObject(url, TMDBMoviePageDTO.class);
            
        } catch (Exception e) {
            log.error("Error al obtener películas por género de TMDB", e);
            return null;
        }
    }
    
    /**
     * Construye la URL completa del póster de una película.
     * 
     * @param posterPath Path del póster devuelto por TMDB
     * @return URL completa del póster
     */
    public String getPosterUrl(String posterPath) {
        return imageConfig.getPosterUrl(posterPath);
    }
    
    /**
     * Construye la URL completa del backdrop de una película.
     * 
     * @param backdropPath Path del backdrop devuelto por TMDB
     * @return URL completa del backdrop
     */
    public String getBackdropUrl(String backdropPath) {
        return imageConfig.getBackdropUrl(backdropPath);
    }
}
