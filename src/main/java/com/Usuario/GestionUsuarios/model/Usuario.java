package com.Usuario.GestionUsuarios.model;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table (name = "usuarios")
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {

    @Id
    @Column(name = "id_usuario")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_completo",nullable = false, length = 100)
    private String nombre;

    @Column(name = "gmail",unique = true, length = 100)
    private String gmail;

    @Column(nullable = false, length = 100)
    private String rol; // Administrador, tecnico, cliente.

    @Column(name = "password",nullable = false, length = 255)
    private String contrasena;
}
