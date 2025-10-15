package com.moviestats.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO para la solicitud de registro de usuario.
 * Contiene los datos necesarios para registrar un nuevo usuario.
 * Este DTO se utiliza en el endpoint de registro para recibir los datos del frontend.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RegisterRequest {

    /** Nombre del usuario (campo obligatorio para el registro) */
    private String name;

    /** Apellido del usuario (campo obligatorio para el registro) */
    private String last_name;

    /** Correo electrónico del usuario (debe ser único, se valida en el servicio) */
    private String email;

    /** Contraseña del usuario (se enviará en texto plano y se hasheará en el servicio para seguridad) */
    private String password;
}
