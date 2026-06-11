package com.Usuario.GestionUsuarios.Controller;

import com.Usuario.GestionUsuarios.dto.UsuarioRequestDTO;
import com.Usuario.GestionUsuarios.dto.UsuarioResponseDTO;
import com.Usuario.GestionUsuarios.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

//Evalucion 3:
// Anotaciones : @Tag, @Operation, @ApiResponse
@RestController
@RequestMapping("/api/usuario")
@RequiredArgsConstructor
@Tag(name = "Gestion de Usuarios",description = "Endpoint para la CRUD de usuarios, autenticacion y filtro de roles.")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    @Operation(summary = "Obtener todos los usuarios", description = "Retorna una colección con todos los usuarios registrados junto a enlaces hipermedia (HATEOAS).")
    @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida exitosamente")
    public ResponseEntity<List<UsuarioResponseDTO>> obtenerTodo(){
        return ResponseEntity.ok(usuarioService.obtenerTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un usuario por su ID", description = "Busca un usuario específico en el sistema utilizando su identificador único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado con éxito"),
            @ApiResponse(responseCode = "404", description = "El ID proporcionado no pertenece a ningún usuario")
    })
    public ResponseEntity<UsuarioResponseDTO> obtenerPorId(@PathVariable Long id){
        return  usuarioService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Registrar un nuevo usuario", description = "Crea un usuario en el sistema. Valida campos obligatorios de entrada.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o con formato incorrecto")
    })
    public ResponseEntity<UsuarioResponseDTO> agregar(@Valid @RequestBody UsuarioRequestDTO dto){
        UsuarioResponseDTO nuevoUsuario = usuarioService.registrarUsuario(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un usuario existente", description = "Modifica los datos del usuario correspondiente al ID enviado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario actualizado con éxito"),
            @ApiResponse(responseCode = "400", description = "Datos de actualización inválidos"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado para actualizar")
    })
    public ResponseEntity<UsuarioResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody UsuarioRequestDTO dto){
        return usuarioService.actualizar(id,dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un usuario", description = "Remueve permanentemente un usuario del sistema mediante su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuario eliminado con éxito (Sin contenido en la respuesta)"),
            @ApiResponse(responseCode = "404", description = "No existe un usuario con el ID especificado")
    })
    public ResponseEntity<Void> eliminar(@PathVariable Long id){
        if(usuarioService.obtenerPorId(id).isEmpty()){
            return ResponseEntity.notFound().build();
        }
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build(); // 204
    }

    @PostMapping("/login")
    @Operation(summary = "Autenticar un usuario", description = "Valida las credenciales de un usuario (Gmail y Contraseña) para permitir el acceso.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autenticación exitosa"),
            @ApiResponse(responseCode = "401", description = "Credenciales incorrectas o usuario no autorizado")
    })
    public ResponseEntity<UsuarioResponseDTO> autenticar(@RequestBody UsuarioRequestDTO credenciales) {
                return usuarioService.autenticar(credenciales.getGmail(),credenciales.getContrasena())
                        .map(ResponseEntity::ok)
                        .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    @GetMapping("/rol/{rol}")
    @Operation(summary = "Filtrar usuarios por Rol", description = "Devuelve un listado hipermedia filtrado por un rol específico (ej: ADMIN, CLIENTE).")
    @ApiResponse(responseCode = "200", description = "Filtro procesado de forma correcta")
    public ResponseEntity<List<UsuarioResponseDTO>> buscarPorRol(@PathVariable String rol){
        List<UsuarioResponseDTO> usuario = usuarioService.listarPorRol(rol);
        return ResponseEntity.ok(usuario);
    }

    @GetMapping("/gmail/{gmail}")
    @Operation(summary = "Filtrar usuarios por Gmail", description = "Busca coincidencias de correos electrónicos.")
    @ApiResponse(responseCode = "200", description = "Búsqueda por correo realizada")
    public ResponseEntity<List<UsuarioResponseDTO>> filtrarPorGmail(@PathVariable String gmail){
        List<UsuarioResponseDTO> usuario = usuarioService.filtrarPorGmail(gmail);
        return ResponseEntity.ok(usuario);
    }

    @GetMapping("/tecnicos")
    @Operation(summary = "Listar usuarios Técnicos", description = "Devuelve de forma directa el segmento de usuarios registrados con funciones técnicas.")
    @ApiResponse(responseCode = "200", description = "Listado técnico recuperado")
    public ResponseEntity<List<UsuarioResponseDTO>> listarTecnico(){
        return ResponseEntity.ok(usuarioService.listarTecnicos());

    }


}
