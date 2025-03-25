package com.example.demo.repository;

import com.example.demo.model.Curso;
import com.example.demo.model.CursoEgresso;
import com.example.demo.model.Egresso;
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
public class CursoEgressoRepositoryTest {

    @Autowired
    private CursoEgressoRepository cursoEgressoRepository;

    @Autowired
    private EgressoRepository egressoRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private CoordenadorRepository coordenadorRepository;  // Para associar um coordenador, se necessário

    private Egresso egresso;
    private Curso curso;
    private CursoEgresso cursoEgresso;

    @BeforeEach
    public void setUp() {
        // Criando e salvando um Egresso
        egresso = new Egresso();
        egresso.setNome("João Silva");
        egresso.setEmail("joao@example.com");
        egresso.setDescricao("Engenheiro de Software");
        egresso.setFoto("foto_url");
        egresso.setLinkedin("linkedin_url");
        egresso.setInstagam("instagram_url");
        egresso.setCurriculo("curriculo_url");
        egresso = egressoRepository.save(egresso);

        // Criando e salvando um Coordenador
        Coordenador coordenador = new Coordenador();
        coordenador.setLogin("coordenador01");
        coordenador.setSenha("senha123");
        coordenador.setTipo("interno");
        coordenador = coordenadorRepository.save(coordenador);

        // Criando e salvando um Curso
        curso = new Curso();
        curso.setNome("Engenharia de Computação");
        curso.setNivel("Superior");
        curso.setCoordenador(coordenador); // Associando o coordenador
        curso = cursoRepository.save(curso);

        // Criando a instância de CursoEgresso
        cursoEgresso = new CursoEgresso();
        cursoEgresso.setAnoInicio(2020);
        cursoEgresso.setAnoFim(2024);
        cursoEgresso.setCurso(curso);
        cursoEgresso.setEgresso(egresso);
    }

    // Testando o save (salvar)
    @Test
    @Transactional
    public void testSaveCursoEgresso() {
        CursoEgresso savedCursoEgresso = cursoEgressoRepository.save(cursoEgresso);

        assertNotNull(savedCursoEgresso.getIdCursoEgresso(), "ID do CursoEgresso não pode ser nulo após salvar");
        assertEquals(cursoEgresso.getAnoInicio(), savedCursoEgresso.getAnoInicio(), "Ano de início deve ser o mesmo após salvar");
        assertEquals(cursoEgresso.getAnoFim(), savedCursoEgresso.getAnoFim(), "Ano de fim deve ser o mesmo após salvar");
        assertEquals(cursoEgresso.getCurso().getIdCurso(), savedCursoEgresso.getCurso().getIdCurso(), "ID do curso deve ser o mesmo");
        assertEquals(cursoEgresso.getEgresso().getIdEgresso(), savedCursoEgresso.getEgresso().getIdEgresso(), "ID do egresso deve ser o mesmo");
    }

    // Testando o find (buscar)
    @Test
    @Transactional
    public void testFindCursoEgressoById() {
        CursoEgresso savedCursoEgresso = cursoEgressoRepository.save(cursoEgresso);

        CursoEgresso foundCursoEgresso = cursoEgressoRepository.findById(savedCursoEgresso.getIdCursoEgresso()).orElse(null);

        assertNotNull(foundCursoEgresso, "CursoEgresso deve ser encontrado por ID");
        assertEquals(savedCursoEgresso.getIdCursoEgresso(), foundCursoEgresso.getIdCursoEgresso(), "ID do CursoEgresso deve ser o mesmo");
    }

    // Testando o update (atualizar)
    @Test
    @Transactional
    public void testUpdateCursoEgresso() {
        CursoEgresso savedCursoEgresso = cursoEgressoRepository.save(cursoEgresso);

        savedCursoEgresso.setAnoInicio(2021);
        savedCursoEgresso.setAnoFim(2025);

        CursoEgresso updatedCursoEgresso = cursoEgressoRepository.save(savedCursoEgresso);

        assertNotNull(updatedCursoEgresso, "CursoEgresso atualizado deve ser salvo");
        assertEquals(2021, updatedCursoEgresso.getAnoInicio(), "Ano de início deve ser atualizado");
        assertEquals(2025, updatedCursoEgresso.getAnoFim(), "Ano de fim deve ser atualizado");
    }

    // Testando o delete (deletar)
    @Test
    @Transactional
    public void testDeleteCursoEgresso() {
        CursoEgresso savedCursoEgresso = cursoEgressoRepository.save(cursoEgresso);

        cursoEgressoRepository.delete(savedCursoEgresso);

        CursoEgresso foundCursoEgresso = cursoEgressoRepository.findById(savedCursoEgresso.getIdCursoEgresso()).orElse(null);

        assertNull(foundCursoEgresso, "CursoEgresso não deve ser encontrado após deletar");
    }
}