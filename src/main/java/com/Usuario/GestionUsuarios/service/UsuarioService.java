package com.Usuario.GestionUsuarios.service;

import com.Usuario.GestionUsuarios.model.Usuario;
import com.Usuario.GestionUsuarios.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;


    private UsuarioResponseDTO mapToDTO(Usuario usuario){
        return new Usuario

    }


}
