package com.Usuario.GestionUsuarios.Confi;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

//Descripsion de la api.
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI usuarioOpenAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("API de Gestion de Usuarios - Plantaforma Hardware y Desemblaje")
                                .version("1.0.0")
                                .description("Documentacion de los endpoints para administrador: Administradores, Tecnicos, y Cliente   ")
                                .contact(new Contact()
                                        .name("Nombre del equipo: Dylan fernandes")
                                        .email("dy.fernandezl@duocuc.cl")))
                .servers(List.of(new Server().url("http://localhost:8080").description("API Gateway Server (Produccion)"),
                        new Server().url("http://localhost:8081").description("API Gateway Server (Desarrollo)")

                ));


    }
}
