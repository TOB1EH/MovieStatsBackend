package com.moviestats.controllers;


public class Constants {
    /**
     * URL base principal de la API.
     * Representa el prefijo común para todos los endpoints de la API.
     */
    public static final String URL_API = "/api";

    /**
     * Versión de la API.
     * Se utiliza para versionar los endpoints de la API y soportar cambios futuros.
    */
    public static final String URL_API_VERSION = "/v1";

    /**
     * URL base completa combinando {@link #URL_API} y {@link #URL_API_VERSION}.
     * Esta constante se utiliza como prefijo para todos los endpoints versionados.
     */
    public static final String URL_BASE = URL_API + URL_API_VERSION;

    
    public static final String URL_MOVIES = URL_BASE + "/pelicula";

}
