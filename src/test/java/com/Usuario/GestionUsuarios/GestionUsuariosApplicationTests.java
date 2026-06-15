package com.Usuario.GestionUsuarios;

import com.Usuario.GestionUsuarios.repository.UsuarioRepository;
import com.Usuario.GestionUsuarios.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;

@SpringBootTest // <- Para realizar las pruebas de integracion
@AutoConfigureMockMvc // <- Para inyectar la MockMvc
@ExtendWith(MockitoExtension.class) // <-
class GestionUsuariosApplicationTests {

	@Mock
	private UsuarioRepository usuarioRepository;

	@InjectMocks
	private UsuarioService

	@Test
	void contextLoads() {
	}

}
