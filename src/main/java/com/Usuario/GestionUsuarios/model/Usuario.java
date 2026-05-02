package com.Usuario.GestionUsuarios.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table (name = "Usuarios")
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "El nombre no puede estar vacio")
    private String nombre;

    @Column(unique = true, length = 100)
    @NotBlank(message = "El gmail del usuario no puede estar vacio")
    private String gmail;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "El rol del usuario no puede estar vacio.")
    private String rol; // Administrador, tecnico, cliente.

    @Column(nullable = false, length = 100)
    @NotBlank(message = "La contraseña no puede esta vacio.")
    @Size(min = 8, max = 8, message = "La contraseña solo puede tener 8 caracteres ")
    private String contrasena;






}
