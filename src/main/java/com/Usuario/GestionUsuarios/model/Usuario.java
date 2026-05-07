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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(unique = true, length = 100)
    private String gmail;

    @Column(nullable = false, length = 100)
    private String rol; // Administrador, tecnico, cliente.

    @Column(nullable = false, length = 100)
    private String contrasena;






}
