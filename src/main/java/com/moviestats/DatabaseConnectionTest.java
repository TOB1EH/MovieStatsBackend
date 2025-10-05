package com.moviestats;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * Componente de prueba de conexión a la base de datos.
 * Esta clase verifica la conectividad con la base de datos PostgreSQL al iniciar la aplicación.
 * Implementa CommandLineRunner para ejecutar la verificación automáticamente durante el arranque.
 *
 * Utiliza JdbcTemplate para ejecutar consultas simples y mostrar información de conexión.
 */
@Component
public class DatabaseConnectionTest implements CommandLineRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Método ejecutado al iniciar la aplicación.
     * Realiza una consulta a la base de datos para obtener el nombre de la base de datos
     * y el usuario actual, mostrando un mensaje de éxito o error en la consola.
     *
     * @param args argumentos de línea de comandos (no utilizados)
     * @throws Exception si ocurre un error durante la consulta
     */
    @Override
    public void run(String... args) throws Exception {
        try {
            // Consultar el nombre de la base de datos actual
            String databaseName = jdbcTemplate.queryForObject("SELECT current_database();", String.class);
            String userName = jdbcTemplate.queryForObject("SELECT current_user;", String.class);

            System.out.println("✅ ÉXITO: Conectado a la base de datos '" + databaseName + "' como usuario '" + userName + "'");
        } catch (Exception e) {
            System.err.println("❌ FALLÓ: No se pudo conectar a la base de datos. Error: " + e.getMessage());
        }
    }
}
