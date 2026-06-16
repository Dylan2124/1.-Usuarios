package com.Usuario.GestionUsuarios.model;

public enum Rol {
    ADMINISTRADOR("Administrador"),
    TECNICO("Técnico de Ensamblaje"),
    USUARIO("Usuario");

    private final String displayName;

    Rol(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    // Devuelve el nombre del enum (ADMINISTRADOR, TECNICO, USUARIO)
    public String getEnumName() {
        return this.name();
    }
}
