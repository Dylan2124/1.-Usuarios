package com.Usuario.GestionUsuarios.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.RepresentationModel; // <-- IMPORTANTE
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(
        name = "UsuarioResponse",
        description = "DTO de respuesta que contiene la información pública del usuario (sin contraseña por seguridad)"
)
public class UsuarioResponseDTO extends RepresentationModel<UsuarioResponseDTO> {
    @Schema(
            description = "Identificador único del usuario en el sistema",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long id;

    @Schema(
            description = "Nombre completo del usuario",
            example = "Juan Pérez García",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String nombre;

    @Schema(
            description = "Correo electrónico del usuario (único en el sistema)",
            example = "juan.perez@example.com",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String gmail;

    @Schema(
            description = "Rol del usuario en el sistema",
            example = "ADMINISTRADOR",
            allowableValues = {"ADMINISTRADOR", "TECNICO", "USUARIO"},
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String rol;
}