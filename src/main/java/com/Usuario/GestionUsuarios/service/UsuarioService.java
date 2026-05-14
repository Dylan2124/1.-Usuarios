package com.Usuario.GestionUsuarios.service;

import com.Usuario.GestionUsuarios.dto.UsuarioRequestDTO;
import com.Usuario.GestionUsuarios.dto.UsuarioResponseDTO;
import com.Usuario.GestionUsuarios.model.Usuario;
import com.Usuario.GestionUsuarios.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Slf4j
@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    private UsuarioResponseDTO mapToDTO(Usuario usuario){
        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getGmail(),
                usuario.getRol()
                // No se llama a la contrasena por temas de seguridad para el usuario.
        );
    }

    // Obtener por id
    public Optional<UsuarioResponseDTO> obtenerPorId(Long id){
        return usuarioRepository.findById(id)
                .map(this::mapToDTO);

    }

    // Obtener todos los usuarios.
    public List<UsuarioResponseDTO> obtenerTodos(){
        return usuarioRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

    }

    // Agregar usuario
    public UsuarioResponseDTO registrarUsuario(UsuarioRequestDTO dto){
        log.info("Iniciando registros de nuevo usuario: {}",dto.getNombre());
        if (usuarioRepository.existsByGmail(dto.getGmail())){
            log.error("Usuario ya registrado");
            throw new RuntimeException("El correo ya esta registrado");
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setGmail(dto.getGmail());
        usuario.setRol(dto.getRol());
        //Encriptacion de la contraseña
        // Ver en config
        String contraseñaEncriptada = passwordEncoder.encode(dto.getContrasena());
        usuario.setContrasena(contraseñaEncriptada);
        usuario = usuarioRepository.save(usuario);
        log.info("Usuario creado con exitosamente con ID: {}",usuario.getId());
        return  mapToDTO(usuario);
    }

    //auntenticar usuario por gmail
    public Optional<UsuarioResponseDTO> autenticar(String gmail,String contrasena){
        log.info("Intento de login para usuario: {} ",gmail);
        return usuarioRepository.encontrarParaAutenticacion(gmail)
                // Comparacion de contraseñas
                .filter(u ->passwordEncoder.matches(contrasena, u.getContrasena()))
                .map(this::mapToDTO);
    }

    // Actualizar por id
    public Optional<UsuarioResponseDTO> actulizar(Long id, UsuarioRequestDTO dto) {
        log.info("Iniciando actualizacion del ususario con ID: {}", id);
        return usuarioRepository.findById(id).map(existente -> {
            existente.setNombre(dto.getNombre());
            existente.setGmail(dto.getGmail());
            existente.setRol(dto.getRol());
            // Actulizacion de la contraseña
            if (dto.getContrasena() != null && !dto.getContrasena().isEmpty()){
                existente.setContrasena(passwordEncoder.encode(dto.getContrasena()));
            }
            Usuario actualizado = usuarioRepository.save(existente);
            log.info("Usuario ID {} actualizado exitosamente",actualizado.getId());
            return  mapToDTO(actualizado);

        });
    }

    // eliminar por id
    public void eliminar(Long id){
        log.info("Eliminando el usuario ID: {}",id);
        if(usuarioRepository.existsById(id)){
            usuarioRepository.deleteById(id);
        }else{
            throw new RuntimeException("Usuario no encontrado");
        }
    }

    // Listar usuario por rol
    public List<UsuarioResponseDTO> listarPorRol (String rol) {
        log.info("Buscando lista de usuarios por su rol: {} ", rol);
        return usuarioRepository.buscarPorRol(rol)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Buscar por gmail
    public List<UsuarioResponseDTO> filtrarPorGmail(String gmail){
        log.info("Filtrando usuario por gmail: {}",gmail);
        return usuarioRepository.filtrarUsuarioPorGmail(gmail)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Buscar los usuario tecnicos
    public List<UsuarioResponseDTO> listarTecnicos(){
        log.info("Consultando lista de tecnioc de ensamblaje disponible");
        return  usuarioRepository.listarTecnicosDisponible()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<UsuarioResponseDTO> optenerPorNombre(String nombre){
        return usuarioRepository.findByNombreContainingIgnoreCase(nombre)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<UsuarioResponseDTO> optenerPorRol(String rol){
        return usuarioRepository.findByRolContainingIgnoreCase(rol)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<UsuarioResponseDTO> optenerPorGmail(String gmail){
        return usuarioRepository.findByGmailContainingIgnoreCase(gmail)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

    }





}

