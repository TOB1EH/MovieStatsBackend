package com.moviestats.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad que representa a un usuario en el sistema MovieStats.
 * Esta clase mapea la tabla "USERS" en la base de datos y contiene la información
 * básica de los usuarios registrados en la plataforma, incluyendo credenciales
 * de autenticación y roles para control de acceso.
 *
 * Utiliza Lombok para generar automáticamente constructores, getters y setters.
 */
@Entity
@Table(name = "USERS")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Usuario {

    /** Identificador único del usuario, generado automáticamente por la base de datos */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idUsuario;

    /** Nombre del usuario (máximo 50 caracteres) */
    @Column(length = 50, unique = false)
    private String nombre;

    /** Apellido del usuario (máximo 50 caracteres) */
    @Column(length = 50, unique = false)
    private String apellido;

    /** Correo electrónico único del usuario, utilizado como identificador de login (máximo 50 caracteres) */
    @Column(length = 50, unique=true)
    private String correo;

    /** Contraseña encriptada del usuario para autenticación */
    private String contrasenia;

    /** Rol del usuario en el sistema (ej: USER, ADMIN) para control de permisos */
    private String rol;
}
