package com.Usuario.GestionUsuarios.repository;

import com.Usuario.GestionUsuarios.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario,Long> {

    List<Usuario> findByNombreContainingIgnoreCase(String nombre);

    List<Usuario> findByRolContainingIgnoreCase(String rol);

    List<Usuario> findByGmailContainingIgnoreCase(String gmail);
    Boolean existsByGmail(String gmail);

    // Buscar rol de los usuario que pertenese a un rol especifico
    @Query("SELECT u FROM Usuario u WHERE u.rol = :rol ")
    List<Usuario> buscarPorRol(@Param("rol")String rol);

    @Query("SELECT u FROM Usuario u WHERE u.gmail = :gmail")
    Optional<Usuario> encontrarParaAutenticacion(@Param("gmail") String gmail);

    // Buscar al tecnico
    @Query("SELECT u FROM Usuario u WHERE u.rol = 'Tecnico de Emsamblaje' ORDER BY u.nombre ASC")
    List<Usuario> listarTecnicosDisponible();

    @Query(value = "SELECT * FROM Usuario WHERE gmail LIKE %:gmail%", nativeQuery = true)
    List<Usuario> filtrarUsuarioPorGmail(@Param("gmail") String gmail);


}
