package com.example.demo.repository;

import com.example.demo.model.Egresso;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EgressoRepositoryTest {

    @Autowired
    private EgressoRepository egressoRepository;

    private Egresso egresso;

    @BeforeEach
    public void setUp() {
        // Criando e configurando a instância do Egresso
        egresso = new Egresso();
        egresso.setNome("Maria Souza");
        egresso.setEmail("maria.souza@example.com");
        egresso.setDescricao("Analista de Dados");
        egresso.setFoto("foto_url");
        egresso.setLinkedin("linkedin_url");
        egresso.setInstagam("instagram_url");
        egresso.setCurriculo("curriculo_url");
    }

    // Testando o save (salvar)
    @Test
    @Transactional
    public void testSaveEgresso() {
        Egresso savedEgresso = egressoRepository.save(egresso);

        assertNotNull(savedEgresso.getIdEgresso(), "ID do Egresso não pode ser nulo após salvar");
        assertEquals(egresso.getNome(), savedEgresso.getNome(), "Nome deve ser o mesmo após salvar");
        assertEquals(egresso.getEmail(), savedEgresso.getEmail(), "Email deve ser o mesmo após salvar");
        assertEquals(egresso.getDescricao(), savedEgresso.getDescricao(), "Descrição deve ser a mesma após salvar");
    }

    // Testando o find (buscar)
    @Test
    @Transactional
    public void testFindEgressoById() {
        Egresso savedEgresso = egressoRepository.save(egresso);

        Egresso foundEgresso = egressoRepository.findById(savedEgresso.getIdEgresso()).orElse(null);

        assertNotNull(foundEgresso, "Egresso deve ser encontrado por ID");
        assertEquals(savedEgresso.getIdEgresso(), foundEgresso.getIdEgresso(), "ID do Egresso deve ser o mesmo");
        assertEquals(savedEgresso.getNome(), foundEgresso.getNome(), "Nome do Egresso deve ser o mesmo");
    }

    // Testando o update (atualizar)
    @Test
    @Transactional
    public void testUpdateEgresso() {
        Egresso savedEgresso = egressoRepository.save(egresso);

        savedEgresso.setNome("Maria Souza Silva");
        savedEgresso.setDescricao("Gerente de TI");

        Egresso updatedEgresso = egressoRepository.save(savedEgresso);

        assertNotNull(updatedEgresso, "Egresso atualizado deve ser salvo");
        assertEquals("Maria Souza Silva", updatedEgresso.getNome(), "Nome deve ser atualizado");
        assertEquals("Gerente de TI", updatedEgresso.getDescricao(), "Descrição deve ser atualizada");
    }

    // Testando o delete (deletar)
    @Test
    @Transactional
    public void testDeleteEgresso() {
        Egresso savedEgresso = egressoRepository.save(egresso);

        egressoRepository.delete(savedEgresso);

        Egresso foundEgresso = egressoRepository.findById(savedEgresso.getIdEgresso()).orElse(null);

        assertNull(foundEgresso, "Egresso não deve ser encontrado após deletar");
    }
}