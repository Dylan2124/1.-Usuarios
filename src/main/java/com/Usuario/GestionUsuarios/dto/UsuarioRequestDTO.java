package com.Usuario.GestionUsuarios.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        name = "UsuarioRequest",
        description = "DTO para crear o actualizar un usuario"
)
public class UsuarioRequestDTO {

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    @Schema(
            description = "Nombre completo del usuario",
            example = "Juan Pérez García",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String nombre;

    @Email(message = "El correo debe ser válido")
    @NotBlank(message = "El correo (gmail) no puede estar vacío")
    @Schema(
            description = "Correo electrónico único del usuario",
            example = "juan.perez@example.com",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String gmail;

    @NotBlank(message = "El rol del usuario no puede estar vacío")
    @Schema(
            description = "Rol del usuario (ADMINISTRADOR, TECNICO, USUARIO)",
            example = "ADMINISTRADOR",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String rol;

    @NotBlank(message = "La contraseña del usuario no puede estar vacía")
    @Size(min = 8, max = 8, message = "La contraseña debe tener exactamente 8 caracteres")
    @Schema(
            description = "Contraseña del usuario (será encriptada con BCrypt)",
            example = "MiCont45",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.WRITE_ONLY
    )
    private String contrasena;
}
