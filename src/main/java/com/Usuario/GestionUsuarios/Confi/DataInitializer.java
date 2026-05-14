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

        repository.save(new Usuario(null,"Juan torres","admin@hardware.cl","Admin del sistema","admin123"));
        repository.save(new Usuario(null,"Felipe contreras","felipe.ensamblaje@hardware.cl","Tecnico de ensamblaje","tec12345"));
        repository.save(new Usuario(null,"Diego ","diego@gmail.cl","Cliente","cliente1"));


    }
}
