package com.Usuario.GestionUsuarios.service;

import com.Usuario.GestionUsuarios.dto.UsuarioRequestDTO;
import com.Usuario.GestionUsuarios.dto.UsuarioResponseDTO;
import com.Usuario.GestionUsuarios.model.Usuario;
import com.Usuario.GestionUsuarios.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    private UsuarioResponseDTO mapToDTO(Usuario usuario){
        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getGmail(),
                usuario.getRol(),
                usuario.getContrasena()
        );
    }

    // Obtener todos los usuarios.
    public List<UsuarioResponseDTO> obtenerTodos(){
        return usuarioRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

    }
    // Buscar usuario por id
    public Optional<UsuarioResponseDTO> obtenerPorId(Long id){
        return usuarioRepository.findById(id).map(this::mapToDTO);
    }

    // Agregar usuario
    public UsuarioResponseDTO guardarUsuario(UsuarioResponseDTO dto){
        .findBy
    }







}
