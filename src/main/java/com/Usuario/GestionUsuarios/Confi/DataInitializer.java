package com.Usuario.GestionUsuarios.Confi;

import com.Usuario.GestionUsuarios.model.Rol;
import com.Usuario.GestionUsuarios.model.Usuario;
import com.Usuario.GestionUsuarios.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args){
        if(repository.count() > 0){
            log.info(">>> Base de datos de usuarios ya tiene datos. Omitiendo inicializacion");
            return;
        }
        log.info(">>> Cargando usuario y roles iniciales para la Plataforma de Hardware.");

        // ADMINISTRADORES
        repository.save(new Usuario(null, "Juan Torres", "admin@hardware.cl", Rol.ADMINISTRADOR , passwordEncoder.encode("adm12345")));
        repository.save(new Usuario(null, "Maria Lopez", "m.lopez@hardware.cl", Rol.ADMINISTRADOR, passwordEncoder.encode("mira9123")));

        // TÉCNICOS DE ENSAMBLAJE
        repository.save(new Usuario(null, "Felipe Contreras", "f.contreras@hardware.cl", Rol.TECNICO, passwordEncoder.encode("tec7777")));
        repository.save(new Usuario(null, "Carla Mendez", "c.mendez@hardware.cl", Rol.TECNICO, passwordEncoder.encode("fixPC123")));
        repository.save(new Usuario(null, "Andrés Silva", "a.silva@hardware.cl", Rol.TECNICO, passwordEncoder.encode("hardw234")));

        // CLIENTES
        repository.save(new Usuario(null, "Diego Gomez", "diego.g@gmail.com", Rol.USUARIO, passwordEncoder.encode("diego123")));
        repository.save(new Usuario(null, "Ana Rojas", "ana.rojas@gmail.com", Rol.USUARIO, passwordEncoder.encode("ana00000")));
        repository.save(new Usuario(null, "Roberto Jara", "rjara@outlook.com", Rol.USUARIO, passwordEncoder.encode("rob9123")));
        repository.save(new Usuario(null, "Lucía Soto", "lucia.soto@vtr.net", Rol.USUARIO, passwordEncoder.encode("luci5123")));
        repository.save(new Usuario(null, "Esteban Quito", "equito@gmail.com", Rol.USUARIO, passwordEncoder.encode("est7743")));

    }
}
