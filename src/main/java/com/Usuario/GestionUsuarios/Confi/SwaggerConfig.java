package com.Usuario.GestionUsuarios.Confi;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

//Documentacion tecnica de la api.
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI usuarioOpenAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("API de Gestión de Usuarios")
                        .version("1.0.0")
                        .description("Microservicio encargado de la gestión completa de usuarios (CRUD), autenticación y control de roles. " +
                                "Forma parte del sistema de Hardware y Desemblaje.")
                        .contact(new Contact()
                                .name("Dylan Fernandes")
                                .email("dy.fernandezl@duocuc.cl")
                                .url("https://github.com/Dylan2124/1.-Usuarios.git"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("API Gateway - Producción"),
                        new Server().url("http://localhost:8081").description("Microservicio de Usuarios - Desarrollo")
                ));
    }
}
