package com.moviestats;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseConnectionTest implements CommandLineRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

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