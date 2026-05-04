package com.Usuario.GestionUsuarios.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRequestDTO {

    @NotBlank(message = "El nombre no puede estar vacio")
    private String nombre;

    @NotBlank(message = "El gmail no puede estar vacio")
    private String gmail;

    @NotBlank(message = "El rol del usuario no puede estar vacio.")
    private String rol;

    @NotBlank(message = "La contraseña del usuario no puede estar vacio." )
    private String contrasena;
}
