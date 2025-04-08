package com.example.demo.configuration;

import com.example.demo.model.Coordenador;
import com.example.demo.model.Egresso;
import com.example.demo.repository.EgressoRepository;
import com.example.demo.repository.CoordenadorRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Component
public class SuperUserInitializer implements CommandLineRunner {

    private final CoordenadorRepository coordenadorRepository;
    private final EgressoRepository egressoRepository;

    @Value("${superuser.login}")
    private String superUserLogin;

    @Value("${superuser.password}")
    private String superUserPassword;

    @Value("${superuser.type}")
    private String superUserType;

    public SuperUserInitializer(CoordenadorRepository coordenadorRepository, EgressoRepository egressoRepository) {
        this.coordenadorRepository = coordenadorRepository;
        this.egressoRepository = egressoRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Verificar se o superusuário já existe
        if (coordenadorRepository.findByLogin(superUserLogin).isEmpty()) {
            Coordenador superUser = new Coordenador();
            superUser.setLogin(superUserLogin);
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            superUser.setSenha(encoder.encode(superUserPassword));
            superUser.setTipo("super");

            coordenadorRepository.save(superUser);
        System.out.println("Superusuário criado com sucesso: " + superUserLogin);
        // Criar e salvar Egresso associado (dados fixos)
        Egresso egresso = new Egresso();
        egresso.setNome("Egresso Superuser");
        egresso.setEmail("superuser@portal.com");
        egresso.setDescricao("Egresso associado ao superusuário.");

        egressoRepository.save(egresso);
        System.out.println("Egresso associado criado com sucesso.");
        } else {
            System.out.println("Superusuário já existe: " + superUserLogin);
        }
    }
}

