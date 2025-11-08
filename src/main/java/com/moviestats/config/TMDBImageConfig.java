package com.moviestats.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración para URLs de imágenes de TMDB.
 */
@Configuration
@ConfigurationProperties(prefix = "tmdb.image")
public class TMDBImageConfig {
    
    private String baseUrl;
    private String posterSize;
    private String backdropSize;
    
    public String getBaseUrl() {
        return baseUrl;
    }
    
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
    
    public String getPosterSize() {
        return posterSize;
    }
    
    public void setPosterSize(String posterSize) {
        this.posterSize = posterSize;
    }
    
    public String getBackdropSize() {
        return backdropSize;
    }
    
    public void setBackdropSize(String backdropSize) {
        this.backdropSize = backdropSize;
    }
    
    /**
     * Construye la URL completa del póster.
     */
    public String getPosterUrl(String posterPath) {
        if (posterPath == null || posterPath.isEmpty()) {
            return null;
        }
        return baseUrl + "/" + posterSize + posterPath;
    }
    
    /**
     * Construye la URL completa del backdrop.
     */
    public String getBackdropUrl(String backdropPath) {
        if (backdropPath == null || backdropPath.isEmpty()) {
            return null;
        }
        return baseUrl + "/" + backdropSize + backdropPath;
    }
}
