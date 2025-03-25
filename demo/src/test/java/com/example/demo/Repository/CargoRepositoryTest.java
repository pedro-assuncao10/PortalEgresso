package com.example.demo.repository;

import com.example.demo.model.Cargo;
import com.example.demo.model.Egresso;
import com.example.demo.repository.CargoRepository;
import com.example.demo.repository.EgressoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CargoRepositoryTest {

    @Autowired
    private CargoRepository cargoRepository;

    @Autowired
    private EgressoRepository egressoRepository;

    private Egresso egresso;
    private Cargo cargo;

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

        // Criando e configurando a instância de Cargo
        cargo = new Cargo();
        cargo.setDescricao("Desenvolvedor Backend");
        cargo.setLocal("Tech Company");
        cargo.setAnoInicio(2022);
        cargo.setAnoFim(2025);
        cargo.setEgresso(egresso);
    }

    // Testando o save (salvar)
    @Test
    @Transactional
    public void testSaveCargo() {
        Cargo savedCargo = cargoRepository.save(cargo);

        assertNotNull(savedCargo.getIdCargo(), "ID do Cargo não pode ser nulo após salvar");
        assertEquals(cargo.getDescricao(), savedCargo.getDescricao(), "Descrição do Cargo deve ser a mesma após salvar");
        assertEquals(cargo.getLocal(), savedCargo.getLocal(), "Local do Cargo deve ser o mesmo após salvar");
        assertEquals(cargo.getEgresso().getIdEgresso(), savedCargo.getEgresso().getIdEgresso(), "Egresso vinculado deve ser o mesmo");
    }

    // Testando o find (buscar)
    @Test
    @Transactional
    public void testFindCargoById() {
        Cargo savedCargo = cargoRepository.save(cargo);

        Cargo foundCargo = cargoRepository.findById(savedCargo.getIdCargo()).orElse(null);

        assertNotNull(foundCargo, "Cargo deve ser encontrado por ID");
        assertEquals(savedCargo.getIdCargo(), foundCargo.getIdCargo(), "ID do Cargo deve ser o mesmo");
        assertEquals(savedCargo.getDescricao(), foundCargo.getDescricao(), "Descrição do Cargo deve ser a mesma");
        assertEquals(savedCargo.getEgresso().getIdEgresso(), foundCargo.getEgresso().getIdEgresso(), "Egresso vinculado deve ser o mesmo");
    }

    // Testando o update (atualizar)
    @Test
    @Transactional
    public void testUpdateCargo() {
        Cargo savedCargo = cargoRepository.save(cargo);

        savedCargo.setDescricao("Desenvolvedor Full Stack");
        savedCargo.setLocal("Tech Innovators");

        Cargo updatedCargo = cargoRepository.save(savedCargo);

        assertNotNull(updatedCargo, "Cargo atualizado deve ser salvo");
        assertEquals("Desenvolvedor Full Stack", updatedCargo.getDescricao(), "Descrição deve ser atualizada");
        assertEquals("Tech Innovators", updatedCargo.getLocal(), "Local deve ser atualizado");
    }

    // Testando o delete (deletar)
    @Test
    @Transactional
    public void testDeleteCargo() {
        Cargo savedCargo = cargoRepository.save(cargo);

        cargoRepository.delete(savedCargo);

        Cargo foundCargo = cargoRepository.findById(savedCargo.getIdCargo()).orElse(null);

        assertNull(foundCargo, "Cargo não deve ser encontrado após deletar");
    }
}