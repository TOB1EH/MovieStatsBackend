package com.moviestats;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


/**
 * Clase de pruebas unitarias para la aplicación MovieStats Backend.
 * Esta clase verifica que el contexto de Spring Boot se carga correctamente,
 * asegurando que todas las configuraciones, beans y dependencias se inicialicen
 * sin errores durante el arranque de la aplicación.
 */
@SpringBootTest
class BackendApplicationTests {

	/**
	 * Prueba básica que verifica si el contexto de la aplicación se carga exitosamente.
	 * Si esta prueba falla, indica problemas en la configuración de Spring Boot,
	 * inyección de dependencias o configuración de la base de datos.
	 */
	@Test
	void contextLoads() {
	}

}
