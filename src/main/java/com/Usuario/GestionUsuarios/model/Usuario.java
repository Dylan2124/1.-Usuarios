package com.Usuario.GestionUsuarios.model;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table (name = "usuarios",schema = "usuario_db")
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

    @Enumerated(EnumType.STRING)
    @Column(name = "rol", nullable = false, length = 20)
    private Rol rol; // ADMINISTRADOR, TECNICO, USUARIO

    @Column(name = "password",nullable = false, length = 255)
    private String contrasena;
}
