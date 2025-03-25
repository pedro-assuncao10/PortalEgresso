package com.example.demo.repository;

import com.example.demo.model.Coordenador;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CoordenadorRepositoryTest {

    @Autowired
    private CoordenadorRepository coordenadorRepository;

    private Coordenador coordenador;

    @BeforeEach
    public void setUp() {
        coordenador = new Coordenador();
        coordenador.setLogin("coordenador123");
        coordenador.setSenha("senha123");
        coordenador.setTipo("admin");
    }

    // Testando o save (salvar)
    @Test
    @Transactional
    public void testSaveCoordenador() {
        Coordenador savedCoordenador = coordenadorRepository.save(coordenador);

        assertNotNull(savedCoordenador.getIdCoordenador(), "ID do coordenador não pode ser nulo após salvar");
        assertEquals(coordenador.getLogin(), savedCoordenador.getLogin(), "Login deve ser o mesmo após salvar");
        assertEquals(coordenador.getTipo(), savedCoordenador.getTipo(), "Tipo deve ser o mesmo após salvar");
    }

    // Testando o find (buscar)
    @Test
    @Transactional
    public void testFindCoordenadorById() {
        Coordenador savedCoordenador = coordenadorRepository.save(coordenador);

        Coordenador foundCoordenador = coordenadorRepository.findById(savedCoordenador.getIdCoordenador()).orElse(null);

        assertNotNull(foundCoordenador, "Coordenador deve ser encontrado por ID");
        assertEquals(savedCoordenador.getLogin(), foundCoordenador.getLogin(), "Login deve ser o mesmo");
    }

    // Testando o update (atualizar)
    @Test
    @Transactional
    public void testUpdateCoordenador() {
        Coordenador savedCoordenador = coordenadorRepository.save(coordenador);

        savedCoordenador.setLogin("novoLogin");
        savedCoordenador.setSenha("novaSenha");

        Coordenador updatedCoordenador = coordenadorRepository.save(savedCoordenador);

        assertNotNull(updatedCoordenador, "Coordenador atualizado deve ser salvo");
        assertEquals("novoLogin", updatedCoordenador.getLogin(), "Login deve ser atualizado");
        assertEquals("novaSenha", updatedCoordenador.getSenha(), "Senha deve ser atualizada");
    }

    // Testando o delete (deletar)
    @Test
    @Transactional
    public void testDeleteCoordenador() {
        Coordenador savedCoordenador = coordenadorRepository.save(coordenador);

        coordenadorRepository.delete(savedCoordenador);

        Coordenador foundCoordenador = coordenadorRepository.findById(savedCoordenador.getIdCoordenador()).orElse(null);

        assertNull(foundCoordenador, "Coordenador não deve ser encontrado após deletar");
    }
}