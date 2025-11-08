package com.moviestats.service;

import com.moviestats.dto.tmdb.TMDBGenreDTO;
import com.moviestats.dto.tmdb.TMDBMovieDTO;
import com.moviestats.dto.tmdb.TMDBMoviePageDTO;
import com.moviestats.model.Genero;
import com.moviestats.model.Pelicula;
import com.moviestats.model.persistence.GeneroRepository;
import com.moviestats.model.persistence.PeliculaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Servicio de sincronización automática con TMDB.
 * Se encarga de importar películas desde TMDB y guardarlas en la base de datos local.
 */
@Service
public class TMDBSyncService {
    
    private static final Logger log = LoggerFactory.getLogger(TMDBSyncService.class);
    
    @Autowired
    private TMDBService tmdbService;
    
    @Autowired
    private PeliculaRepository peliculaRepository;
    
    @Autowired
    private GeneroRepository generoRepository;
    
    // Mapeo de IDs de géneros de TMDB a nombres locales
    private static final Map<Integer, String> TMDB_GENRE_MAP = new HashMap<>();
    
    static {
        // Mapeo manual de géneros TMDB a tus géneros locales
        TMDB_GENRE_MAP.put(28, "Acción");
        TMDB_GENRE_MAP.put(12, "Aventura");
        TMDB_GENRE_MAP.put(16, "Animación");
        TMDB_GENRE_MAP.put(35, "Comedia");
        TMDB_GENRE_MAP.put(80, "Crimen");
        TMDB_GENRE_MAP.put(99, "Documental");
        TMDB_GENRE_MAP.put(18, "Drama");
        TMDB_GENRE_MAP.put(10751, "Familia");
        TMDB_GENRE_MAP.put(14, "Fantasía");
        TMDB_GENRE_MAP.put(36, "Histórico");
        TMDB_GENRE_MAP.put(27, "Terror");
        TMDB_GENRE_MAP.put(10402, "Musical");
        TMDB_GENRE_MAP.put(9648, "Misterio");
        TMDB_GENRE_MAP.put(10749, "Romance");
        TMDB_GENRE_MAP.put(878, "Ciencia Ficción");
        TMDB_GENRE_MAP.put(10770, "Drama");
        TMDB_GENRE_MAP.put(53, "Thriller");
        TMDB_GENRE_MAP.put(10752, "Bélico");
        TMDB_GENRE_MAP.put(37, "Western");
    }
    
    /**
     * Importa películas populares desde TMDB.
     * 
     * @param cantidad Número de películas a importar
     * @return Número de películas importadas exitosamente
     */
    public int importPopularMovies(int cantidad) {
        log.info("Iniciando importación de {} películas populares desde TMDB", cantidad);
        
        int importadas = 0;
        int page = 1;
        int peliculasPorPagina = 20; // TMDB devuelve 20 por página
        int paginasNecesarias = (int) Math.ceil((double) cantidad / peliculasPorPagina);
        
        for (int i = 0; i < paginasNecesarias && importadas < cantidad; i++) {
            TMDBMoviePageDTO pageDTO = tmdbService.getPopularMovies(page);
            
            if (pageDTO == null || pageDTO.getResults() == null) {
                log.warn("No se pudo obtener la página {} de películas populares", page);
                break;
            }
            
            for (TMDBMovieDTO tmdbMovie : pageDTO.getResults()) {
                if (importadas >= cantidad) break;
                
                try {
                    // Obtener detalles completos (incluye runtime y géneros completos)
                    TMDBMovieDTO fullMovie = tmdbService.getMovieDetails(tmdbMovie.getId());
                    
                    if (fullMovie != null) {
                        try {
                            if (importMovieFromTMDB(fullMovie)) {
                                importadas++;
                                log.info("Película importada: {} ({}/{})", fullMovie.getTitle(), importadas, cantidad);
                            }
                        } catch (RuntimeException e) {
                            // La transacción individual falló, continuar con la siguiente
                            log.warn("No se pudo importar '{}' (ID: {}), continuando...", 
                                    fullMovie.getTitle(), fullMovie.getId());
                        }
                    }
                    
                    // Pequeña pausa para no sobrecargar la API
                    Thread.sleep(250);
                    
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log.warn("Importación interrumpida");
                    break;
                } catch (Exception e) {
                    log.error("Error al obtener detalles de película TMDB ID: {}", tmdbMovie.getId(), e);
                }
            }
            
            page++;
        }
        
        log.info("Importación completada. Total: {} películas importadas", importadas);
        return importadas;
    }
    
    /**
     * Importa películas mejor valoradas.
     * 
     * @param cantidad Número de películas a importar
     * @return Número de películas importadas
     */
    public int importTopRatedMovies(int cantidad) {
        log.info("Iniciando importación de {} películas mejor valoradas desde TMDB", cantidad);
        
        int importadas = 0;
        int page = 1;
        int peliculasPorPagina = 20;
        int paginasNecesarias = (int) Math.ceil((double) cantidad / peliculasPorPagina);
        
        for (int i = 0; i < paginasNecesarias && importadas < cantidad; i++) {
            TMDBMoviePageDTO pageDTO = tmdbService.getTopRatedMovies(page);
            
            if (pageDTO == null || pageDTO.getResults() == null) {
                break;
            }
            
            for (TMDBMovieDTO tmdbMovie : pageDTO.getResults()) {
                if (importadas >= cantidad) break;
                
                try {
                    TMDBMovieDTO fullMovie = tmdbService.getMovieDetails(tmdbMovie.getId());
                    
                    if (fullMovie != null) {
                        try {
                            if (importMovieFromTMDB(fullMovie)) {
                                importadas++;
                                log.info("Película importada: {} ({}/{})", fullMovie.getTitle(), importadas, cantidad);
                            }
                        } catch (RuntimeException e) {
                            log.warn("No se pudo importar '{}' (ID: {}), continuando...", 
                                    fullMovie.getTitle(), fullMovie.getId());
                        }
                    }
                    
                    Thread.sleep(250);
                    
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log.warn("Importación interrumpida");
                    break;
                } catch (Exception e) {
                    log.error("Error al obtener detalles de película TMDB ID: {}", tmdbMovie.getId(), e);
                }
            }
            
            page++;
        }
        
        log.info("Importación completada. Total: {} películas importadas", importadas);
        return importadas;
    }
    
    /**
     * Busca e importa películas por query.
     * 
     * @param query Término de búsqueda
     * @param cantidad Máximo de películas a importar
     * @return Número de películas importadas
     */
    @Transactional
    public int searchAndImportMovies(String query, int cantidad) {
        log.info("Buscando e importando películas: '{}' (máx: {})", query, cantidad);
        
        int importadas = 0;
        TMDBMoviePageDTO searchResults = tmdbService.searchMovies(query, 1);
        
        if (searchResults == null || searchResults.getResults() == null) {
            log.warn("No se encontraron resultados para: {}", query);
            return 0;
        }
        
        for (TMDBMovieDTO tmdbMovie : searchResults.getResults()) {
            if (importadas >= cantidad) break;
            
            try {
                TMDBMovieDTO fullMovie = tmdbService.getMovieDetails(tmdbMovie.getId());
                
                if (fullMovie != null) {
                    try {
                        if (importMovieFromTMDB(fullMovie)) {
                            importadas++;
                            log.info("Película importada: {} ({}/{})", fullMovie.getTitle(), importadas, cantidad);
                        }
                    } catch (RuntimeException e) {
                        log.warn("No se pudo importar '{}' (ID: {}), continuando...", 
                                fullMovie.getTitle(), fullMovie.getId());
                    }
                }
                
                Thread.sleep(250);
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.warn("Importación interrumpida");
                break;
            } catch (Exception e) {
                log.error("Error al obtener detalles de película TMDB ID: {}", tmdbMovie.getId(), e);
            }
        }
        
        return importadas;
    }
    
    /**
     * Importa una película individual desde TMDB.
     * Verifica si ya existe antes de importar.
     * 
     * @param tmdbId ID de la película en TMDB
     * @return true si se importó exitosamente, false si ya existía o hubo error
     */
    public boolean importSingleMovie(Long tmdbId) {
        try {
            TMDBMovieDTO tmdbMovie = tmdbService.getMovieDetails(tmdbId);
            
            if (tmdbMovie == null) {
                log.warn("No se encontró la película TMDB ID: {}", tmdbId);
                return false;
            }
            
            return importMovieFromTMDB(tmdbMovie);
        } catch (RuntimeException e) {
            log.error("Error al importar película TMDB ID: {}", tmdbId, e);
            return false;
        }
    }
    
    /**
     * Convierte y guarda una película de TMDB en la base de datos local.
     * Cada película se guarda en su propia transacción para evitar que un error afecte a otras.
     * 
     * @param tmdbMovie Película de TMDB
     * @return true si se guardó exitosamente
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    private boolean importMovieFromTMDB(TMDBMovieDTO tmdbMovie) {
        try {
            // Verificar si ya existe (por nombre y año)
            if (tmdbMovie.getTitle() == null || tmdbMovie.getTitle().isEmpty()) {
                log.warn("Película sin título, ignorando TMDB ID: {}", tmdbMovie.getId());
                return false;
            }
            
            Optional<Pelicula> existente = peliculaRepository.findByNombre(tmdbMovie.getTitle());
            if (existente.isPresent()) {
                log.info("La película '{}' ya existe en la BD, saltando", tmdbMovie.getTitle());
                return false;
            }
            
            Pelicula pelicula = new Pelicula();
            
            // Datos básicos
            pelicula.setNombre(tmdbMovie.getTitle());
            pelicula.setSinopsis(tmdbMovie.getOverview());
            pelicula.setPuntuacion(tmdbMovie.getVoteAverage());
            pelicula.setVotos(tmdbMovie.getVoteCount());
            
            // Fecha de salida
            if (tmdbMovie.getReleaseDate() != null && !tmdbMovie.getReleaseDate().isEmpty()) {
                try {
                    LocalDate localDate = LocalDate.parse(tmdbMovie.getReleaseDate(), DateTimeFormatter.ISO_DATE);
                    pelicula.setFechaSalida(Date.valueOf(localDate));
                } catch (Exception e) {
                    log.warn("Error al parsear fecha: {}", tmdbMovie.getReleaseDate());
                }
            }
            
            // Duración
            if (tmdbMovie.getRuntime() != null) {
                pelicula.setDuracion(tmdbMovie.getRuntime());
            }
            
            // Imagen (URL del póster)
            if (tmdbMovie.getPosterPath() != null) {
                pelicula.setImagen(tmdbService.getPosterUrl(tmdbMovie.getPosterPath()));
            }
            
            // Idioma original
            pelicula.setIdioma(tmdbMovie.getOriginalLanguage());
            
            // Clasificación por edad (basada en el campo "adult" de TMDB)
            // TMDB no proporciona clasificaciones específicas como PG-13, R, etc. en el endpoint básico
            // Usamos una aproximación basada en si es contenido adulto
            if (tmdbMovie.getAdult() != null && tmdbMovie.getAdult()) {
                pelicula.setClasificacion("R"); // Contenido adulto = R (Restricted)
            } else {
                // Para no adultos, usamos PG-13 como clasificación por defecto
                // (la mayoría de películas populares están en este rango)
                pelicula.setClasificacion("PG-13");
            }
            
            // Guardar película primero
            pelicula = peliculaRepository.save(pelicula);
            
            // Asignar géneros
            if (tmdbMovie.getGenres() != null && !tmdbMovie.getGenres().isEmpty()) {
                Set<Genero> generos = new HashSet<>();
                
                for (TMDBGenreDTO tmdbGenre : tmdbMovie.getGenres()) {
                    String nombreGeneroLocal = TMDB_GENRE_MAP.get(tmdbGenre.getId());
                    
                    if (nombreGeneroLocal != null) {
                        Optional<Genero> generoOpt = generoRepository.findByNombre(nombreGeneroLocal);
                        generoOpt.ifPresent(generos::add);
                    }
                }
                
                pelicula.setGenero(new ArrayList<>(generos));
                peliculaRepository.save(pelicula);
            }
            
            log.debug("Película guardada: {} con {} géneros", pelicula.getNombre(), 
                     pelicula.getGenero() != null ? pelicula.getGenero().size() : 0);
            
            return true;
            
        } catch (Exception e) {
            log.error("Error al importar película TMDB ID: {}", 
                     tmdbMovie != null ? tmdbMovie.getId() : "null", e);
            // El rollbackFor=Exception.class asegura que la transacción se revierta
            throw new RuntimeException("Error al importar película: " + 
                                      (tmdbMovie != null ? tmdbMovie.getTitle() : "unknown"), e);
        }
    }
}
