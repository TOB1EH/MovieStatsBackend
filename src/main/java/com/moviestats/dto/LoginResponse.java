package com.moviestats.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Objeto de transferencia de datos (DTO) que representa la
 * respuesta generada tras un inicio de sesión exitoso en el
 * sistema MovieStats.
 * <p>
 * Esta clase contiene el token JWT emitido por el servidor,
 * el cual se utiliza para autenticar las solicitudes posteriores
 * del usuario dentro de la aplicación.
 * </p>
 *
 * <p><b>Uso principal:</b> Es devuelto por el método
 * {@code AuthController.login(LoginRequest loginRequest)} cuando
 * las credenciales del usuario son válidas.</p>
 *
 * <p><b>Ejemplo JSON de respuesta:</b></p>
 * <pre>
 * {
 *   "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
 * }
 * </pre>
 * @version 1.0
 * @see com.moviestats.controller.AuthController
 */
@Getter
@AllArgsConstructor
public class LoginResponse {
    /**
     * Token JWT generado tras una autenticación exitosa.
     * <p>
     * Este token debe ser incluido en el encabezado
     * <code>Authorization</code> de las solicitudes posteriores
     * (con el prefijo <code>Bearer</code>) para acceder a recursos protegidos.
     * </p>
     */
    private String token;
}