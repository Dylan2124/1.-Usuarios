package com.Usuario.GestionUsuarios.Confi;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//Descripsion de la api.
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI usuarioOpenAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("API de Microservisio de Usuario")
                        .description("Documentacion para la gestion de usuarios, credenciales y roles")
                        .version("1.0.0"));
    }
}
