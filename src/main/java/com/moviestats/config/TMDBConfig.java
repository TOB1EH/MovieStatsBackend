package com.moviestats.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración para TMDB API.
 * Lee las propiedades desde application.properties con prefijo "tmdb.api".
 */
@Configuration
@ConfigurationProperties(prefix = "tmdb.api")
public class TMDBConfig {
    
    private String key;
    private String accessToken;
    private String baseUrl;
    private String language;
    
    // Getters y Setters
    
    public String getKey() {
        return key;
    }
    
    public void setKey(String key) {
        this.key = key;
    }
    
    public String getAccessToken() {
        return accessToken;
    }
    
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
    
    public String getBaseUrl() {
        return baseUrl;
    }
    
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
    
    public String getLanguage() {
        return language;
    }
    
    public void setLanguage(String language) {
        this.language = language;
    }
}
