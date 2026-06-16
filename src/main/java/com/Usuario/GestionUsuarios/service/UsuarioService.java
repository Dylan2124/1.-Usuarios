package com.Usuario.GestionUsuarios.service;

import com.Usuario.GestionUsuarios.dto.UsuarioRequestDTO;
import com.Usuario.GestionUsuarios.dto.UsuarioResponseDTO;
import com.Usuario.GestionUsuarios.model.Usuario;
import com.Usuario.GestionUsuarios.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
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
                usuario.getRol().name()
                // No se llama a la contrasena en el dto de repuesta por seguridad.
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
        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setGmail(dto.getGmail());
        // Convertir rol recibido como String a enum Rol
        usuario.setRol(parseRolOrThrow(dto.getRol()));
        //Encriptacion de la contraseña

        String contrasenaEncriptada = passwordEncoder.encode(dto.getContrasena());
        usuario.setContrasena(contrasenaEncriptada);
        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        return new UsuarioResponseDTO(
                usuarioGuardado.getId(),
                usuarioGuardado.getNombre(),
                usuarioGuardado.getGmail(),
                usuarioGuardado.getRol().name()
        );

    }

    //auntenticar usuario por gmail
    public Optional<UsuarioResponseDTO> autenticar(String gmail,String contrasena){
        return usuarioRepository.encontrarParaAutenticacion(gmail)
                // Comparacion de contraseñas
                .filter(u ->passwordEncoder.matches(contrasena, u.getContrasena()))
                .map(this::mapToDTO);
    }

    // Actualizar por id
    public Optional<UsuarioResponseDTO> actualizar(Long id, UsuarioRequestDTO dto) {
        return usuarioRepository.findById(id).map(existente -> {
            existente.setNombre(dto.getNombre());
            existente.setGmail(dto.getGmail());
            existente.setRol(parseRolOrThrow(dto.getRol()));

            // Actulizacion de la contraseña
            if (dto.getContrasena() != null && !dto.getContrasena().isEmpty()){
                existente.setContrasena(passwordEncoder.encode(dto.getContrasena()));
            }
            Usuario actualizado = usuarioRepository.save(existente);
            return  mapToDTO(actualizado);

        });
    }

    // Convierte un String a Rol (case-insensitive) con mapeo flexible de sinónimos.
    // Lanza RuntimeException si no es válido.
    private com.Usuario.GestionUsuarios.model.Rol parseRolOrThrow(String rolStr){
        if (rolStr == null) throw new RuntimeException("El rol no puede ser nulo");
        String normalized = normalize(rolStr);

        // Mapeo de sinónimos comunes a constantes del enum
        if (normalized.matches(".*\b(admin|administrador|administradordelistema|administrador del sistema)\b.*")){
            return com.Usuario.GestionUsuarios.model.Rol.ADMINISTRADOR;
        }
        if (normalized.matches(".*\b(tecnico|t[eé]cnico|tecnico de ensamblaje|t[eé]cnico de ensamblaje|tecnico_ensamblaje)\b.*")){
            return com.Usuario.GestionUsuarios.model.Rol.TECNICO;
        }
        if (normalized.matches(".*\b(usuario|cliente|cliente final)\b.*")){
            return com.Usuario.GestionUsuarios.model.Rol.USUARIO;
        }

        // Intentar coincidencia directa con el nombre del enum
        try{
            return com.Usuario.GestionUsuarios.model.Rol.valueOf(normalized.toUpperCase());
        }catch (IllegalArgumentException e){
            throw new RuntimeException("Rol inválido: " + rolStr);
        }
    }

    // Normalize: trim, toLowerCase, remove accents and non-alphanumeric (preserve spaces)
    private String normalize(String s){
        String t = s.trim().toLowerCase();
        String noAccents = Normalizer.normalize(t, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
        // Replace punctuation with spaces to ease regex matching
        return noAccents.replaceAll("[^a-z0-9\\s]", " ").replaceAll("\\s+", " ").trim();
    }

    // eliminar por id
    public void eliminar(Long id){
        if(usuarioRepository.existsById(id)){
            usuarioRepository.deleteById(id);
        }else{
            throw new RuntimeException("Usuario no encontrado");
        }
    }

    // Listar usuario por rol
    public List<UsuarioResponseDTO> listarPorRol (String rol) {
        try {
            com.Usuario.GestionUsuarios.model.Rol enumRol = com.Usuario.GestionUsuarios.model.Rol.valueOf(rol.toUpperCase());
            return usuarioRepository.buscarPorRol(enumRol)
                    .stream()
                    .map(this::mapToDTO)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            // Rol no válido: retornar lista vacía
            return List.of();
        }
    }

    // Buscar por gmail
    public List<UsuarioResponseDTO> filtrarPorGmail(String gmail){
        return usuarioRepository.filtrarUsuarioPorGmail(gmail)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Buscar los usuario tecnicos
    public List<UsuarioResponseDTO> listarTecnicos(){
        return  usuarioRepository.listarTecnicosDisponible()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<UsuarioResponseDTO> obtenerPorNombre(String nombre){
        return usuarioRepository.findByNombreContainingIgnoreCase(nombre)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<UsuarioResponseDTO> obtenerPorRol(String rol){
        try {
            com.Usuario.GestionUsuarios.model.Rol enumRol = com.Usuario.GestionUsuarios.model.Rol.valueOf(rol.toUpperCase());
            return usuarioRepository.findByRol(enumRol)
                    .stream()
                    .map(this::mapToDTO)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            return List.of();
        }
    }

    public List<UsuarioResponseDTO> obtenerPorGmail(String gmail){
        return usuarioRepository.findByGmailContainingIgnoreCase(gmail)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

    }

}

