package com.moviestats;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal de la aplicación Spring Boot para MovieStats.
 * Esta clase inicia la aplicación backend que proporciona servicios para la plataforma
 * de estadísticas de películas, incluyendo gestión de usuarios, películas, géneros y votos.
 *
 * La anotación @SpringBootApplication habilita la configuración automática de Spring Boot,
 * incluyendo el escaneo de componentes, configuración de JPA y configuración web.
 */
@SpringBootApplication
public class BackendApplication {

	/**
	 * Método principal que inicia la aplicación Spring Boot.
	 * Ejecuta el contexto de Spring y arranca el servidor embebido.
	 *
	 * @param args argumentos de línea de comandos (no utilizados en esta aplicación)
	 */
	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

}
