package com.example.demo.repository;

import com.example.demo.model.Curso;
import com.example.demo.model.Coordenador;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CursoRepositoryTest {

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private CoordenadorRepository coordenadorRepository;

    private Coordenador coordenador;

    @BeforeAll
    void setUp() {
        // Cria e salva um Coordenador no banco de dados
        coordenador = new Coordenador();
        coordenador.setLogin("joao.silva");
        coordenador.setSenha("senha123");
        coordenador.setTipo("egresso");
        coordenador = coordenadorRepository.save(coordenador);
    }

    @AfterAll
    void tearDown() {
        // Limpa os dados de teste
        cursoRepository.deleteAll();
        coordenadorRepository.deleteAll();
    }

    @Test
    void testSaveCurso() {
        Curso curso = new Curso();
        curso.setNome("Engenharia de Software");
        curso.setNivel("Graduação");
        curso.setCoordenador(coordenador);

        Curso savedCurso = cursoRepository.save(curso);

        assertNotNull(savedCurso.getIdCurso());
        assertEquals("Engenharia de Software", savedCurso.getNome());
        assertEquals("Graduação", savedCurso.getNivel());
        assertEquals(coordenador.getIdCoordenador(), savedCurso.getCoordenador().getIdCoordenador());
    }

    @Test
    void testFindCursoById() {
        Curso curso = new Curso();
        curso.setNome("Engenharia de Computação");
        curso.setNivel("Graduação");
        curso.setCoordenador(coordenador);
        Curso savedCurso = cursoRepository.save(curso);

        Optional<Curso> foundCurso = cursoRepository.findById(savedCurso.getIdCurso());

        assertTrue(foundCurso.isPresent());
        assertEquals("Engenharia de Computação", foundCurso.get().getNome());
        assertEquals(coordenador.getIdCoordenador(), foundCurso.get().getCoordenador().getIdCoordenador());
    }

    @Test
    void testUpdateCurso() {
        Curso curso = new Curso();
        curso.setNome("Ciência da Computação");
        curso.setNivel("Graduação");
        curso.setCoordenador(coordenador);
        Curso savedCurso = cursoRepository.save(curso);

        savedCurso.setNome("Ciência de Dados");
        Curso updatedCurso = cursoRepository.save(savedCurso);

        assertEquals("Ciência de Dados", updatedCurso.getNome());
    }

    @Test
    void testDeleteCurso() {
        Curso curso = new Curso();
        curso.setNome("Engenharia Civil");
        curso.setNivel("Graduação");
        curso.setCoordenador(coordenador);
        Curso savedCurso = cursoRepository.save(curso);

        cursoRepository.delete(savedCurso);

        Optional<Curso> deletedCurso = cursoRepository.findById(savedCurso.getIdCurso());
        assertFalse(deletedCurso.isPresent());
    }
}