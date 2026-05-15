package com.Usuario.GestionUsuarios.Confi;

import com.Usuario.GestionUsuarios.model.Usuario;
import com.Usuario.GestionUsuarios.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository repository;

    @Override
    public void run(String... args){
        if(repository.count() > 0){
            log.info(">>> Base de datos de usuarios ya tiene datos. Omitiendo inicializacion");
            return;
        }
        log.info(">>> Cargando usuario y roles iniciales para la Plataforma de Hardware.");

        // ADMINISTRADORES
        repository.save(new Usuario(null, "Juan Torres", "admin@hardware.cl", "Administrador", "adm123"));
        repository.save(new Usuario(null, "Maria Lopez", "m.lopez@hardware.cl", "Administrador", "mira9"));

        // TÉCNICOS DE ENSAMBLAJE
        repository.save(new Usuario(null, "Felipe Contreras", "f.contreras@hardware.cl", "Técnico de Ensamblaje", "tec77"));
        repository.save(new Usuario(null, "Carla Mendez", "c.mendez@hardware.cl", "Técnico de Ensamblaje", "fixPC"));
        repository.save(new Usuario(null, "Andrés Silva", "a.silva@hardware.cl", "Técnico de Ensamblaje", "hardw2"));

        // CLIENTES
        repository.save(new Usuario(null, "Diego Gomez", "diego.g@gmail.com", "Cliente", "diego1"));
        repository.save(new Usuario(null, "Ana Rojas", "ana.rojas@gmail.com", "Cliente", "ana00"));
        repository.save(new Usuario(null, "Roberto Jara", "rjara@outlook.com", "Cliente", "rob9"));
        repository.save(new Usuario(null, "Lucía Soto", "lucia.soto@vtr.net", "Cliente", "luci5"));
        repository.save(new Usuario(null, "Esteban Quito", "equito@gmail.com", "Cliente", "est77"));

    }
}
