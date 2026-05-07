package com.Usuario.GestionUsuarios.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRequestDTO {

    @NotBlank(message = "El nombre no puede estar vacio")
    private String nombre;

    @Email(message = "Debe ser un correo valido")
    @NotBlank(message = "El gmail no puede estar vacio")
    private String gmail;

    @NotBlank(message = "El rol es obligatorio.")
    private String rol;

    @Size(min = 8, max = 8, message = "La contraseña solo puede tener 8 caracteres ")
    @NotBlank(message = "La contraseña del usuario no puede estar vacio." )
    private String contrasena;
}
