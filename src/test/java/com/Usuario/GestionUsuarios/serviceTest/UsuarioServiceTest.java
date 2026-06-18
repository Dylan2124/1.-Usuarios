package com.Usuario.GestionUsuarios.serviceTest;

import com.Usuario.GestionUsuarios.dto.UsuarioRequestDTO;
import com.Usuario.GestionUsuarios.dto.UsuarioResponseDTO;
import com.Usuario.GestionUsuarios.model.Rol;
import com.Usuario.GestionUsuarios.model.Usuario;
import com.Usuario.GestionUsuarios.repository.UsuarioRepository;
import com.Usuario.GestionUsuarios.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas del Servicio de Usuarios")
class UsuarioServiceTest {
    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuarioMock;
    private UsuarioRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        // ARRANGE: Preparar datos comunes
        usuarioMock = new Usuario();
        usuarioMock.setId(1L);
        usuarioMock.setNombre("Juan Pérez");
        usuarioMock.setGmail("juan@example.com");
        usuarioMock.setRol(Rol.ADMINISTRADOR);
        usuarioMock.setContrasena("MiCont45");

        requestDTO = new UsuarioRequestDTO();
        requestDTO.setNombre("Juan Pérez");
        requestDTO.setGmail("juan@example.com");
        requestDTO.setRol("ADMINISTRADOR");
        requestDTO.setContrasena("MiCont45");
    }

    @Test
    @DisplayName("Debe obtener un usuario por ID exitosamente")
    void testObtenerPorId_Success() {
        // ARRANGE
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioMock));

        // ACT
        Optional<UsuarioResponseDTO> resultado = usuarioService.obtenerPorId(1L);

        // ASSERT
        assertTrue(resultado.isPresent());
        assertEquals("Juan Pérez", resultado.get().getNombre());
        assertEquals("juan@example.com", resultado.get().getGmail());
        verify(usuarioRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Debe retornar Optional vacío cuando el usuario no existe")
    void testObtenerPorId_NotFound() {
        // ARRANGE
        when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());

        // ACT
        Optional<UsuarioResponseDTO> resultado = usuarioService.obtenerPorId(999L);

        // ASSERT
        assertFalse(resultado.isPresent());
        verify(usuarioRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Debe obtener todos los usuarios")
    void testObtenerTodos() {
        // ARRANGE
        List<Usuario> usuarios = List.of(usuarioMock);
        when(usuarioRepository.findAll()).thenReturn(usuarios);

        // ACT
        List<UsuarioResponseDTO> resultado = usuarioService.obtenerTodos();

        // ASSERT
        assertEquals(1, resultado.size());
        assertEquals("Juan Pérez", resultado.get(0).getNombre());
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe registrar un usuario nuevo con contraseña encriptada")
    void testRegistrarUsuario_Success() {
        // ARRANGE
        when(passwordEncoder.encode(requestDTO.getContrasena())).thenReturn("MiCont45_Encriptada");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioMock);

        // ACT
        UsuarioResponseDTO resultado = usuarioService.registrarUsuario(requestDTO);

        // ASSERT
        assertNotNull(resultado);
        assertEquals("Juan Pérez", resultado.getNombre());
        assertEquals(Rol.ADMINISTRADOR.name(), resultado.getRol());
        verify(passwordEncoder, times(1)).encode(requestDTO.getContrasena());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Debe autenticar un usuario con credenciales correctas")
    void testAutenticar_Success() {
        // ARRANGE
        String contrasenaEncriptada = "MiCont45_Encriptada";
        usuarioMock.setContrasena(contrasenaEncriptada);

        when(usuarioRepository.encontrarParaAutenticacion("juan@example.com"))
                .thenReturn(Optional.of(usuarioMock));
        when(passwordEncoder.matches("MiCont45", contrasenaEncriptada))
                .thenReturn(true);

        // ACT
        Optional<UsuarioResponseDTO> resultado = usuarioService.autenticar("juan@example.com", "MiCont45");

        // ASSERT
        assertTrue(resultado.isPresent());
        assertEquals("Juan Pérez", resultado.get().getNombre());
        verify(usuarioRepository, times(1)).encontrarParaAutenticacion("juan@example.com");
    }

    @Test
    @DisplayName("Debe rechazar autenticación con contraseña incorrecta")
    void testAutenticar_IncorrectPassword() {
        // ARRANGE
        when(usuarioRepository.encontrarParaAutenticacion("juan@example.com"))
                .thenReturn(Optional.of(usuarioMock));
        when(passwordEncoder.matches("ContraIncorrecta", usuarioMock.getContrasena()))
                .thenReturn(false);

        // ACT
        Optional<UsuarioResponseDTO> resultado = usuarioService.autenticar("juan@example.com", "ContraIncorrecta");

        // ASSERT
        assertFalse(resultado.isPresent());
    }

    @Test
    @DisplayName("Debe actualizar un usuario existente")
    void testActualizar_Success() {
        // ARRANGE
        UsuarioRequestDTO updateDTO = new UsuarioRequestDTO();
        updateDTO.setNombre("Juan Pérez Actualizado");
        updateDTO.setGmail("juannuevo@example.com");
        updateDTO.setRol("TECNICO");
        updateDTO.setContrasena("NuevaCont32");

        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setId(1L);
        usuarioExistente.setNombre("Juan Pérez");
        usuarioExistente.setGmail("juan@example.com");
        usuarioExistente.setRol(Rol.ADMINISTRADOR);
        usuarioExistente.setContrasena("MiCont45");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioExistente));
        when(passwordEncoder.encode("NuevaCont32")).thenReturn("NuevaCont32_Encriptada");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioExistente);

        // ACT
        Optional<UsuarioResponseDTO> resultado = usuarioService.actualizar(1L, updateDTO);

        // ASSERT
        assertTrue(resultado.isPresent());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
        verify(passwordEncoder, times(1)).encode("NuevaCont32");
    }

    @Test
    @DisplayName("Debe eliminar un usuario existente")
    void testEliminar_Success() {
        // ARRANGE
        when(usuarioRepository.existsById(1L)).thenReturn(true);

        // ACT
        usuarioService.eliminar(1L);

        // ASSERT
        verify(usuarioRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Debe lanzar excepción al eliminar usuario que no existe")
    void testEliminar_NotFound() {
        // ARRANGE
        when(usuarioRepository.existsById(999L)).thenReturn(false);

        // ACT & ASSERT
        assertThrows(RuntimeException.class, () -> usuarioService.eliminar(999L));
    }

    @Test
    @DisplayName("Debe listar usuarios por rol ADMINISTRADOR")
    void testListarPorRol_Admin() {
        // ARRANGE
        when(usuarioRepository.buscarPorRol(Rol.ADMINISTRADOR))
                .thenReturn(List.of(usuarioMock));

        // ACT
        List<UsuarioResponseDTO> resultado = usuarioService.listarPorRol("ADMINISTRADOR");

        // ASSERT
        assertEquals(1, resultado.size());
        assertEquals(Rol.ADMINISTRADOR.name(), resultado.get(0).getRol());
    }

    @Test
    @DisplayName("Debe retornar lista vacía para rol inválido")
    void testListarPorRol_InvalidRol() {
        // ACT
        List<UsuarioResponseDTO> resultado = usuarioService.listarPorRol("ROL_INVALIDO");

        // ASSERT
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("Debe filtrar usuarios por Gmail")
    void testFiltrarPorGmail() {
        // ARRANGE
        when(usuarioRepository.filtrarUsuarioPorGmail("juan"))
                .thenReturn(List.of(usuarioMock));

        // ACT
        List<UsuarioResponseDTO> resultado = usuarioService.filtrarPorGmail("juan");

        // ASSERT
        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0).getGmail().contains("juan"));
    }

    @Test
    @DisplayName("Debe listar usuario técnicos disponibles")
    void testListarTecnicos() {
        // ARRANGE
        Usuario tecnicoMock = new Usuario();
        tecnicoMock.setId(2L);
        tecnicoMock.setNombre("Carlos Técnico");
        tecnicoMock.setGmail("carlos@example.com");
        tecnicoMock.setRol(Rol.TECNICO);
        tecnicoMock.setContrasena("encriptada");

        when(usuarioRepository.listarTecnicosDisponible())
                .thenReturn(List.of(tecnicoMock));

        // ACT
        List<UsuarioResponseDTO> resultado = usuarioService.listarTecnicos();

        // ASSERT
        assertEquals(1, resultado.size());
        assertEquals(Rol.TECNICO.name(), resultado.get(0).getRol());
        verify(usuarioRepository, times(1)).listarTecnicosDisponible();
    }
}
