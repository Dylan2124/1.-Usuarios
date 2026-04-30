package com.Usuario.GestionUsuarios.repository;

import com.Usuario.GestionUsuarios.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario,Long> {

    List<Usuario> findNombreContainigIgnoreCase(String nombre);

    List<Usuario> findRolContaingIgnorecase(String rol);

    List<Usuario> findByGmailContainigIgnoreCase(String gmail);
    Boolean exitsByGmail(String gmail);

    @Query("SELECT U FROM Usuario U WHERE U ")




}
