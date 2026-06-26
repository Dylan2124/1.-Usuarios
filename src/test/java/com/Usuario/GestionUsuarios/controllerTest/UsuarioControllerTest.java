package com.Usuario.GestionUsuarios.controllerTest;
import com.Usuario.GestionUsuarios.dto.UsuarioRequestDTO;
import com.Usuario.GestionUsuarios.dto.UsuarioResponseDTO;
import com.Usuario.GestionUsuarios.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {"eureka.client.enabled=false", "spring.cloud.discovery.enabled=false"})
@AutoConfigureMockMvc(addFilters = false) // <-- ESTA ES LA CLAVE MÁGICA
@DisplayName("Pruebas del Controlador de Usuarios")
class UsuarioControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UsuarioService usuarioService;

    @Autowired
    private ObjectMapper objectMapper;

    private UsuarioResponseDTO usuarioResponse;
    private UsuarioRequestDTO usuarioRequest;

    @BeforeEach
    void setUp() {
        // ARRANGE: Preparar datos de prueba
        usuarioResponse = new UsuarioResponseDTO();
        usuarioResponse.setId(1L);
        usuarioResponse.setNombre("Juan Pérez");
        usuarioResponse.setGmail("juan@example.com");
        usuarioResponse.setRol("ADMINISTRADOR");

        usuarioRequest = new UsuarioRequestDTO();
        usuarioRequest.setNombre("Juan Pérez");
        usuarioRequest.setGmail("juan@example.com");
        usuarioRequest.setRol("ADMINISTRADOR");
        usuarioRequest.setContrasena("MiCont45");
    }

    @Test
    @DisplayName("GET /api/usuarios - Debe retornar lista de usuarios con código 200")
    void testObtenerTodos_Success() throws Exception {
        // ARRANGE
        List<UsuarioResponseDTO> usuarios = List.of(usuarioResponse);
        when(usuarioService.obtenerTodos()).thenReturn(usuarios);

        // ACT & ASSERT
        mockMvc.perform(get("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.usuarioResponseDTOList[0].nombre")
                        .value("Juan Pérez"))
                .andExpect(MockMvcResultMatchers.jsonPath("$._links.self").exists());

        verify(usuarioService, times(1)).obtenerTodos();
    }

    @Test
    @DisplayName("GET /api/usuarios/{id} - Debe retornar usuario específico con código 200")
    void testObtenerPorId_Success() throws Exception {
        // ARRANGE
        when(usuarioService.obtenerPorId(1L)).thenReturn(Optional.of(usuarioResponse));

        // ACT & ASSERT
        mockMvc.perform(get("/api/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.nombre").value("Juan Pérez"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.gmail").value("juan@example.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$._links.self").exists());

        verify(usuarioService, times(1)).obtenerPorId(1L);
    }

    @Test
    @DisplayName("GET /api/usuarios/{id} - Debe retornar 404 cuando usuario no existe")
    void testObtenerPorId_NotFound() throws Exception {
        // ARRANGE
        when(usuarioService.obtenerPorId(999L)).thenReturn(Optional.empty());

        // ACT & ASSERT
        mockMvc.perform(get("/api/usuarios/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(usuarioService, times(1)).obtenerPorId(999L);
    }

    @Test
    @DisplayName("POST /api/usuarios - Debe crear nuevo usuario y retornar 201")
    void testAgregar_Success() throws Exception {
        // ARRANGE
        when(usuarioService.registrarUsuario(any(UsuarioRequestDTO.class)))
                .thenReturn(usuarioResponse);

        // ACT & ASSERT
        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioRequest)))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.nombre").value("Juan Pérez"))
                .andExpect(MockMvcResultMatchers.jsonPath("$._links.self").exists());

        verify(usuarioService, times(1)).registrarUsuario(any(UsuarioRequestDTO.class));
    }

    @Test
    @DisplayName("POST /api/usuarios - Debe retornar 400 con datos inválidos")
    void testAgregar_InvalidData() throws Exception {
        // ARRANGE: DTO con campos inválidos
        UsuarioRequestDTO invalidDTO = new UsuarioRequestDTO();
        invalidDTO.setNombre(""); // Nombre vacío
        invalidDTO.setGmail("no-es-email"); // Email inválido
        invalidDTO.setRol("ROLEVALIDOINVALIDO");
        invalidDTO.setContrasena("corta"); // Contraseña muy corta

        // ACT & ASSERT
        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());

        verify(usuarioService, never()).registrarUsuario(any());
    }

    @Test
    @DisplayName("PUT /api/usuarios/{id} - Debe actualizar usuario y retornar 200")
    void testActualizar_Success() throws Exception {
        // ARRANGE
        UsuarioResponseDTO actualizado = new UsuarioResponseDTO();
        actualizado.setId(1L);
        actualizado.setNombre("Juan Pérez Actualizado");
        actualizado.setGmail("juannuevo@example.com");
        actualizado.setRol("TECNICO");

        when(usuarioService.actualizar(eq(1L), any(UsuarioRequestDTO.class)))
                .thenReturn(Optional.of(actualizado));

        // ACT & ASSERT
        mockMvc.perform(put("/api/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioRequest)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.nombre")
                        .value("Juan Pérez Actualizado"));

        verify(usuarioService, times(1)).actualizar(eq(1L), any(UsuarioRequestDTO.class));
    }

    @Test
    @DisplayName("PUT /api/usuarios/{id} - Debe retornar 404 cuando usuario no existe")
    void testActualizar_NotFound() throws Exception {
        // ARRANGE
        when(usuarioService.actualizar(eq(999L), any(UsuarioRequestDTO.class)))
                .thenReturn(Optional.empty());

        // ACT & ASSERT
        mockMvc.perform(put("/api/usuarios/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioRequest)))
                .andExpect(status().isNotFound());

        verify(usuarioService, times(1)).actualizar(eq(999L), any(UsuarioRequestDTO.class));
    }

    @Test
    @DisplayName("DELETE /api/usuarios/{id} - Debe eliminar usuario y retornar 204")
    void testEliminar_Success() throws Exception {
        // ARRANGE
        when(usuarioService.obtenerPorId(1L)).thenReturn(Optional.of(usuarioResponse));
        doNothing().when(usuarioService).eliminar(1L);

        // ACT & ASSERT
        mockMvc.perform(delete("/api/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(usuarioService, atLeastOnce()).obtenerPorId(1L);
        verify(usuarioService, times(1)).eliminar(1L);

    }

    @Test
    @DisplayName("DELETE /api/usuarios/{id} - Debe retornar 404 cuando usuario no existe")
    void testEliminar_NotFound() throws Exception {
        // ARRANGE
        when(usuarioService.obtenerPorId(999L)).thenReturn(Optional.empty());

        // ACT & ASSERT
        mockMvc.perform(delete("/api/usuarios/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(usuarioService, never()).eliminar(999L);
    }

    @Test
    @DisplayName("GET /api/usuarios/rol/TECNICO - Debe filtrar por rol correctamente")
    void testBuscarPorRol() throws Exception {
        // ARRANGE
        UsuarioResponseDTO tecnico = new UsuarioResponseDTO();
        tecnico.setId(2L);
        tecnico.setNombre("Carlos Técnico");
        tecnico.setGmail("carlos@example.com");
        tecnico.setRol("TECNICO");

        when(usuarioService.listarPorRol("TECNICO")).thenReturn(List.of(tecnico));

        // ACT & ASSERT
        mockMvc.perform(get("/api/usuarios/rol/TECNICO")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.usuarioResponseDTOList[0].rol")
                        .value("TECNICO"));

        verify(usuarioService, times(1)).listarPorRol("TECNICO");
    }

    @Test
    @DisplayName("GET /api/usuarios/gmail/juan - Debe filtrar por Gmail correctamente")
    void testFiltrarPorGmail() throws Exception {
        // ARRANGE
        when(usuarioService.filtrarPorGmail("juan")).thenReturn(List.of(usuarioResponse));

        // ACT & ASSERT
        mockMvc.perform(get("/api/usuarios/gmail/juan")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(usuarioService, times(1)).filtrarPorGmail("juan");
    }

    @Test
    @DisplayName("GET /api/usuarios/tecnicos - Debe listar técnicos")
    void testListarTecnicos() throws Exception {
        // ARRANGE
        UsuarioResponseDTO tecnico = new UsuarioResponseDTO();
        tecnico.setId(2L);
        tecnico.setNombre("Carlos Técnico");
        tecnico.setGmail("carlos@example.com");
        tecnico.setRol("TECNICO");

        when(usuarioService.listarTecnicos()).thenReturn(List.of(tecnico));

        // ACT & ASSERT
        mockMvc.perform(get("/api/usuarios/tecnicos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(usuarioService, times(1)).listarTecnicos();
    }
}
