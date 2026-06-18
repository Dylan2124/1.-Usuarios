package com.Usuario.GestionUsuarios.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

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
    @JsonProperty("id")
    private Long id;

    @Schema(
            description = "Nombre completo del usuario",
            example = "Juan Pérez García",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty("nombre")
    private String nombre;

    @Schema(
            description = "Correo electrónico del usuario (único en el sistema)",
            example = "juan.perez@example.com",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty("gmail")
    private String gmail;

    @Schema(
            description = "Rol del usuario en el sistema",
            example = "ADMINISTRADOR",
            allowableValues = {"ADMINISTRADOR", "TECNICO", "USUARIO"},
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty("rol")
    private String rol;
}
