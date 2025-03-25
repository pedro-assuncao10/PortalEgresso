package com.example.demo.repository;

import com.example.demo.model.Depoimento;
import com.example.demo.model.Egresso;
import com.example.demo.repository.DepoimentoRepository;
import com.example.demo.repository.EgressoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DepoimentoRepositoryTest {

    @Autowired
    private DepoimentoRepository depoimentoRepository;

    @Autowired
    private EgressoRepository egressoRepository;

    private Egresso egresso;
    private Depoimento depoimento;

    @BeforeEach
    public void setUp() {
        // Criando e configurando a instância de Egresso
        egresso = new Egresso();
        egresso.setNome("Carlos Silva");
        egresso.setEmail("carlos.silva@example.com");
        egresso.setDescricao("Engenheiro de Software");
        egresso.setFoto("foto_url");
        egresso.setLinkedin("linkedin_url");
        egresso.setInstagam("instagram_url");
        egresso.setCurriculo("curriculo_url");

        // Salvando o Egresso
        egresso = egressoRepository.save(egresso);

        // Criando e configurando a instância de Depoimento
        depoimento = new Depoimento();
        depoimento.setTexto("Excelente experiência na empresa!");
        depoimento.setData(new Date());
        depoimento.setEgresso(egresso);
    }

    // Testando o save (salvar)
    @Test
    @Transactional
    public void testSaveDepoimento() {
        Depoimento savedDepoimento = depoimentoRepository.save(depoimento);

        assertNotNull(savedDepoimento.getIdDepoimento(), "ID do Depoimento não pode ser nulo após salvar");
        assertEquals(depoimento.getTexto(), savedDepoimento.getTexto(), "Texto do Depoimento deve ser o mesmo após salvar");
        assertEquals(depoimento.getEgresso().getIdEgresso(), savedDepoimento.getEgresso().getIdEgresso(), "Egresso vinculado deve ser o mesmo");
        assertNotNull(savedDepoimento.getData(), "Data do Depoimento não pode ser nula");
    }

    // Testando o find (buscar)
    @Test
    @Transactional
    public void testFindDepoimentoById() {
        Depoimento savedDepoimento = depoimentoRepository.save(depoimento);

        Depoimento foundDepoimento = depoimentoRepository.findById(savedDepoimento.getIdDepoimento()).orElse(null);

        assertNotNull(foundDepoimento, "Depoimento deve ser encontrado por ID");
        assertEquals(savedDepoimento.getIdDepoimento(), foundDepoimento.getIdDepoimento(), "ID do Depoimento deve ser o mesmo");
        assertEquals(savedDepoimento.getTexto(), foundDepoimento.getTexto(), "Texto do Depoimento deve ser o mesmo");
        assertEquals(savedDepoimento.getEgresso().getIdEgresso(), foundDepoimento.getEgresso().getIdEgresso(), "Egresso vinculado deve ser o mesmo");
    }

    // Testando o update (atualizar)
    @Test
    @Transactional
    public void testUpdateDepoimento() {
        Depoimento savedDepoimento = depoimentoRepository.save(depoimento);

        savedDepoimento.setTexto("A experiência foi incrível!");
        savedDepoimento.setData(new Date());

        Depoimento updatedDepoimento = depoimentoRepository.save(savedDepoimento);

        assertNotNull(updatedDepoimento, "Depoimento atualizado deve ser salvo");
        assertEquals("A experiência foi incrível!", updatedDepoimento.getTexto(), "Texto deve ser atualizado");
        assertNotNull(updatedDepoimento.getData(), "Data deve ser atualizada");
    }

    // Testando o delete (deletar)
    @Test
    @Transactional
    public void testDeleteDepoimento() {
        Depoimento savedDepoimento = depoimentoRepository.save(depoimento);

        depoimentoRepository.delete(savedDepoimento);

        Depoimento foundDepoimento = depoimentoRepository.findById(savedDepoimento.getIdDepoimento()).orElse(null);

        assertNull(foundDepoimento, "Depoimento não deve ser encontrado após deletar");
    }
}
