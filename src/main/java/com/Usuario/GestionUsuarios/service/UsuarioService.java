package com.Usuario.GestionUsuarios.service;

import com.Usuario.GestionUsuarios.dto.UsuarioRequestDTO;
import com.Usuario.GestionUsuarios.dto.UsuarioResponseDTO;
import com.Usuario.GestionUsuarios.model.Usuario;
import com.Usuario.GestionUsuarios.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Slf4j // Es para imprimir mensajes en  la consol  y rastrear que pasa en la app
@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    private UsuarioResponseDTO mapToDTO(Usuario usuario){
        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getGmail(),
                usuario.getRol()
                // No se llama al contrasena por temas de seguridad.
        );
    }

    // Obtener todos los usuarios.
    public List<UsuarioResponseDTO> obtenerTodos(){
        log.info("Obteniendo lista completa de usuarios");
        return usuarioRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

    }

    // Buscar usuario por id
    public Optional<UsuarioResponseDTO> obtenerPorId(Long id){
        log.info("Buscando usuario con ID: {}", id); // {} <- Para espacio reservado
        return usuarioRepository.findById(id)
                .map(this::mapToDTO);

    }

    // Agregar usuario
    public UsuarioResponseDTO registrarUsuario(UsuarioRequestDTO dto){
        log.info("Iniciando registros de nuevo usuario: {}",dto.getNombre());
        if (usuarioRepository.existsByGmail(dto.getGmail())){
            log.error("El gmail ya esta registrado");
            throw new RuntimeException("El correo exite");
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setGmail(dto.getGmail());
        usuario.setRol(dto.getRol());
        usuario.setContrasena(dto.getContrasena());

        usuario = usuarioRepository.save(usuario);
        log.info("Usuario creado con exitosamente con ID: {}",usuario.getId());
        return  mapToDTO(usuario);
    }

    //auntenticar usuario por gmail
    public Optional<UsuarioResponseDTO> obtenerPorGmail(String gmail){
        log.info("Buscando por el correo: {} ",gmail);
        return usuarioRepository.encontrarParaAutenticacion(gmail)
                .map(this::mapToDTO);


    }


    // Actualizar por id

    // eliminar por id

    // Listar usuario por rol?








}
