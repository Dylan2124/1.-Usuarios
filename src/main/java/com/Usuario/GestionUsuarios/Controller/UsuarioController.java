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
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "Gestion de Usuarios", description = "Endpoint para la CRUD de usuarios, autenticacion y filtro de roles.")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    @Operation(summary = "Obtener todos los usuarios", description = "Retorna una colección con todos los usuarios registrados junto a enlaces hipermedia (HATEOAS).")
    @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida exitosamente")
    public ResponseEntity<CollectionModel<UsuarioResponseDTO>> obtenerTodos() {
        List<UsuarioResponseDTO> usuarios = usuarioService.obtenerTodos();

        // Enlace a cada usuario individual
        usuarios.forEach(usuario ->
                usuario.add(linkTo(methodOn(UsuarioController.class).obtenerPorId(usuario.getId()))
                        .withSelfRel().withTitle("Ver detalles del usuario"))
        );

        // Enlace general de la colección y acción de crear
        CollectionModel<UsuarioResponseDTO> resources = CollectionModel.of(usuarios,
                linkTo(methodOn(UsuarioController.class).obtenerTodos()).withSelfRel().withTitle("Lista completa de usuarios"),
                linkTo(methodOn(UsuarioController.class).agregar(null)).withRel("crear").withTitle("Crear nuevo usuario")
        );

        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un usuario por su ID", description = "Busca un usuario específico en el sistema utilizando su identificador único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado con éxito"),
            @ApiResponse(responseCode = "404", description = "El ID proporcionado no pertenece a ningún usuario")
    })
    public ResponseEntity<UsuarioResponseDTO> obtenerPorId(@PathVariable Long id) {
        return usuarioService.obtenerPorId(id)
                .map(usuario -> {
                    usuario.add(linkTo(methodOn(UsuarioController.class).obtenerPorId(id)).withSelfRel().withTitle("Ver este usuario"));
                    usuario.add(linkTo(methodOn(UsuarioController.class).obtenerTodos()).withRel("usuarios").withTitle("Volver a lista de usuarios"));
                    usuario.add(linkTo(methodOn(UsuarioController.class).actualizar(id, null)).withRel("editar").withTitle("Actualizar este usuario"));
                    usuario.add(linkTo(methodOn(UsuarioController.class).eliminar(id)).withRel("eliminar").withTitle("Eliminar este usuario"));
                    return ResponseEntity.ok(usuario);
                })
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

        nuevoUsuario.add(linkTo(methodOn(UsuarioController.class).obtenerPorId(nuevoUsuario.getId())).withSelfRel().withTitle("Ver usuario creado"));
        nuevoUsuario.add(linkTo(methodOn(UsuarioController.class).obtenerTodos()).withRel("usuarios").withTitle("Lista de usuarios"));

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
        return usuarioService.actualizar(id, dto)
                .map(usuario -> {
                    usuario.add(linkTo(methodOn(UsuarioController.class).obtenerPorId(id)).withSelfRel().withTitle("Ver usuario actualizado"));
                    usuario.add(linkTo(methodOn(UsuarioController.class).obtenerTodos()).withRel("usuarios").withTitle("Volver a lista"));
                    return ResponseEntity.ok(usuario);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un usuario", description = "Remueve permanentemente un usuario del sistema mediante su ID y devuelve un mensaje de confirmación.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario eliminado con éxito"),
            @ApiResponse(responseCode = "404", description = "No existe un usuario con el ID especificado")
    })
    public ResponseEntity<Map<String, String>> eliminar(@PathVariable Long id){
        if(usuarioService.obtenerPorId(id).isEmpty()){
            return ResponseEntity.notFound().build();
        }

        usuarioService.eliminar(id);

        // Creamos una respuesta en formato JSON
        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("mensaje", "El usuario con ID " + id + " ha sido eliminado correctamente.");

        return ResponseEntity.ok(respuesta); // Ahora devuelve 200 OK y el mensaje JSON
    }

    @GetMapping("/rol/{rol}")
    @Operation(summary = "Filtrar usuarios por Rol", description = "Devuelve un listado hipermedia filtrado por un rol específico (ej: ADMIN, CLIENTE).")
    @ApiResponse(responseCode = "200", description = "Filtro procesado de forma correcta")
    public ResponseEntity<CollectionModel<UsuarioResponseDTO>> buscarPorRol(@PathVariable String rol) {
        List<UsuarioResponseDTO> usuarios = usuarioService.listarPorRol(rol);

        usuarios.forEach(usuario -> usuario.add(linkTo(methodOn(UsuarioController.class).obtenerPorId(usuario.getId())).withSelfRel()));

        CollectionModel<UsuarioResponseDTO> resources = CollectionModel.of(usuarios,
                linkTo(methodOn(UsuarioController.class).buscarPorRol(rol)).withSelfRel().withTitle("Usuarios con rol " + rol),
                linkTo(methodOn(UsuarioController.class).obtenerTodos()).withRel("todos").withTitle("Todos los usuarios")
        );

        return ResponseEntity.ok(resources);
    }

    @GetMapping("/gmail/{gmail}")
    @Operation(summary = "Filtrar usuarios por Gmail", description = "Busca coincidencias de correos electrónicos.")
    @ApiResponse(responseCode = "200", description = "Búsqueda por correo realizada")
    public ResponseEntity<CollectionModel<UsuarioResponseDTO>> filtrarPorGmail(@PathVariable String gmail) {
        List<UsuarioResponseDTO> usuarios = usuarioService.filtrarPorGmail(gmail);

        usuarios.forEach(usuario -> usuario.add(linkTo(methodOn(UsuarioController.class).obtenerPorId(usuario.getId())).withSelfRel()));

        CollectionModel<UsuarioResponseDTO> resources = CollectionModel.of(usuarios,
                linkTo(methodOn(UsuarioController.class).filtrarPorGmail(gmail)).withSelfRel().withTitle("Búsqueda por Gmail: " + gmail),
                linkTo(methodOn(UsuarioController.class).obtenerTodos()).withRel("todos").withTitle("Todos los usuarios")
        );

        return ResponseEntity.ok(resources);
    }

    @GetMapping("/tecnicos")
    @Operation(summary = "Listar usuarios Técnicos", description = "Devuelve de forma directa el segmento de usuarios registrados con funciones técnicas.")
    @ApiResponse(responseCode = "200", description = "Listado técnico recuperado")
    public ResponseEntity<CollectionModel<UsuarioResponseDTO>> listarTecnico() {
        List<UsuarioResponseDTO> tecnicos = usuarioService.listarTecnicos();

        tecnicos.forEach(tecnico -> tecnico.add(linkTo(methodOn(UsuarioController.class).obtenerPorId(tecnico.getId())).withSelfRel()));

        CollectionModel<UsuarioResponseDTO> resources = CollectionModel.of(tecnicos,
                linkTo(methodOn(UsuarioController.class).listarTecnico()).withSelfRel().withTitle("Lista de Técnicos"),
                linkTo(methodOn(UsuarioController.class).obtenerTodos()).withRel("todos").withTitle("Todos los usuarios")
        );

        return ResponseEntity.ok(resources);
    }
}