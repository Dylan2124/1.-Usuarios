package com.Usuario.GestionUsuarios.Controller;

import com.Usuario.GestionUsuarios.dto.UsuarioRequestDTO;
import com.Usuario.GestionUsuarios.dto.UsuarioResponseDTO;
import com.Usuario.GestionUsuarios.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/usuario")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> obtenerTodo(){
        return ResponseEntity.ok(usuarioService.obtenerTodos());
    }

    @GetMapping("{id}")
    public ResponseEntity<UsuarioResponseDTO> obtenerPorId(@PathVariable Long id){
        return  usuarioService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> agregar(@Valid @RequestBody UsuarioRequestDTO dto){
        return ResponseEntity.status(201).body(usuarioService.registrarUsuario(dto));
    }

    @PutMapping("{id}")
    public ResponseEntity<UsuarioResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody UsuarioRequestDTO dto){
        return usuarioService.actulizar(id,dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id){
        if(usuarioService.obtenerPorId(id).isEmpty()){
            return ResponseEntity.notFound().build();
        }
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build(); // 204
    }

    @GetMapping("/gmail/{gmail}")
    public ResponseEntity<UsuarioResponseDTO> autenticar(@PathVariable String gmail, String contrasena) {
        return usuarioService.autenticar(gmail,contrasena)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/rol/{rol}")
    public ResponseEntity<List<UsuarioResponseDTO>> buscarPorRol(@PathVariable String rol){
        List<UsuarioResponseDTO> usuario = usuarioService.listarPorRol(rol);
        return ResponseEntity.ok(usuario);
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<UsuarioResponseDTO>> filtrarPorGmail(@RequestParam String gmail){
        List<UsuarioResponseDTO> usuario = usuarioService.filtrarPorGmail(gmail);
        return ResponseEntity.ok(usuario);
    }

    @GetMapping("/tecnicos")
    public ResponseEntity<List<UsuarioResponseDTO>> listarTecnico(){
        return ResponseEntity.ok(usuarioService.listarTecnicos());

    }

}
