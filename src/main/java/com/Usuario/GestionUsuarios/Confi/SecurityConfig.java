package com.Usuario.GestionUsuarios.Confi;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Implementacion de dependencia Spring Security
    // Para asegurar la contraseña del usuario
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //filtro para Postman / Swagger
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                        .requestMatchers("/api/usuario/login").permitAll()
                        // Permisos para el CRUD completo de usuarios:
                        .requestMatchers(HttpMethod.POST, "/api/usuario").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/usuario/**").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/usuario/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/usuario/**").permitAll()
                        .requestMatchers("/api/usuario/**").permitAll()
                        .requestMatchers("/api/usuarios/**", "/api/usuarios").permitAll()

                        .anyRequest().authenticated()
                );
        return http.build();
    }
}