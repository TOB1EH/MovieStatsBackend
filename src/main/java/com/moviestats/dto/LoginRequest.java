package com.moviestats.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Objeto de transferencia de datos (DTO) que representa la solicitud
 * de inicio de sesión de un usuario en el sistema MovieStats.
 * <p>
 * Esta clase se utiliza para recibir las credenciales enviadas
 * desde el cliente (frontend) al realizar una petición de autenticación.
 * Contiene el correo electrónico y la contraseña proporcionados
 * por el usuario.
 * </p>
 *
 * <p><b>Uso principal:</b> Es consumido por el método
 * {@code AuthController.login(LoginRequest loginRequest)} para
 * validar credenciales y generar un token JWT.</p>
 *
 * <p><b>Ejemplo JSON:</b></p>
 * <pre>
 * {
 *   "correo": "usuario@ejemplo.com",
 *   "contrasenia": "miContraseniaSegura"
 * }
 * </pre>
 *
 */
@Getter
@Setter
public class LoginRequest {
    /**
     * Dirección de correo electrónico asociada al usuario
     * que intenta autenticarse.
     * <p>Debe tener un formato válido, por ejemplo:
     * <code>usuario@dominio.com</code>.</p>
     */
    private String correo;

    /**
     * Contraseña del usuario que intenta iniciar sesión.
     * <p>Puede estar en texto plano o encriptada con BCrypt,
     * dependiendo de la configuración del sistema.</p>
     */
    private String contrasenia;
}