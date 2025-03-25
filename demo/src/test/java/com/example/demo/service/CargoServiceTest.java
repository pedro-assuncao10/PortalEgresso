package com.example.demo.service;

import com.example.demo.model.Cargo;
import com.example.demo.model.Egresso;
import com.example.demo.repository.CargoRepository;
import com.example.demo.repository.EgressoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockitoAnnotations;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import java.util.List;
import java.util.Arrays;

@SpringBootTest
public class CargoServiceTest {

    @Mock
    private CargoRepository cargoRepository;

    @Mock
    private EgressoRepository egressoRepository;

    @InjectMocks
    private CargoService cargoService;

    @Test
    public void testCriarCargo() {
        // Dado
        Integer idEgresso = 1;
        Egresso egresso = new Egresso();
        egresso.setIdEgresso(idEgresso);

        Cargo novoCargo = new Cargo();
        novoCargo.setDescricao("Desenvolvedor");
        novoCargo.setLocal("Empresa XYZ");
        novoCargo.setAnoInicio(2020);
        novoCargo.setAnoFim(2023);

        when(egressoRepository.findById(idEgresso)).thenReturn(Optional.of(egresso));
        when(cargoRepository.save(any(Cargo.class))).thenReturn(novoCargo);

        // Quando
        Cargo resultado = cargoService.criarCargo(idEgresso, novoCargo);

        // Então
        assertNotNull(resultado);
        assertEquals("Desenvolvedor", resultado.getDescricao());
        assertEquals("Empresa XYZ", resultado.getLocal());
        assertEquals(2020, resultado.getAnoInicio());
        assertEquals(2023, resultado.getAnoFim());

        // Verificar se o método save do cargoRepository foi chamado
        verify(cargoRepository, times(1)).save(any(Cargo.class));
    }

    @Test
    public void testCriarCargo_EgressoNaoEncontrado() {
        // Dado
        Integer idEgresso = 1;
        Cargo novoCargo = new Cargo();
        novoCargo.setDescricao("Desenvolvedor");

        when(egressoRepository.findById(idEgresso)).thenReturn(Optional.empty());

        // Quando e Então
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cargoService.criarCargo(idEgresso, novoCargo);
        });

        assertEquals("Egresso com ID " + idEgresso + " não encontrado", exception.getMessage());
    }



    //buscas
    @Test
    public void testBuscarCargosPorEgresso() {
        // Arrange
        Integer idEgresso = 1;
        Egresso egresso = new Egresso();
        egresso.setIdEgresso(idEgresso);  // Definindo o Egresso

        Cargo cargo1 = new Cargo();
        cargo1.setIdCargo(1);
        cargo1.setEgresso(egresso);
        cargo1.setDescricao("Cargo1");
        cargo1.setLocal("Local1");
        cargo1.setAnoInicio(2020);
        cargo1.setAnoFim(2022);

        Cargo cargo2 = new Cargo();
        cargo2.setIdCargo(2);
        cargo2.setEgresso(egresso);
        cargo2.setDescricao("Cargo2");
        cargo2.setLocal("Local2");
        cargo2.setAnoInicio(2021);
        cargo2.setAnoFim(2023);

        List<Cargo> cargos = Arrays.asList(cargo1, cargo2);
        when(cargoRepository.findByEgresso_IdEgresso(idEgresso)).thenReturn(cargos);

        // Act
        List<Cargo> result = cargoService.buscarCargosPorEgresso(idEgresso);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Cargo1", result.get(0).getDescricao());
        assertEquals("Cargo2", result.get(1).getDescricao());
        verify(cargoRepository, times(1)).findByEgresso_IdEgresso(idEgresso);
    }


    //atualizar
    private Cargo cargoExistente;
    private Cargo novoCargo;

    @BeforeEach
    void setUp() {
        // Criando cargo existente
        cargoExistente = new Cargo();
        cargoExistente.setIdCargo(1);
        cargoExistente.setDescricao("Cargo Antigo");
        cargoExistente.setLocal("Antigo Local");
        cargoExistente.setAnoInicio(2020);
        cargoExistente.setAnoFim(2022);

        // Criando novo cargo para atualização
        novoCargo = new Cargo();
        novoCargo.setDescricao("Cargo Atualizado");
        novoCargo.setLocal("Novo Local");
        novoCargo.setAnoInicio(2023);
        novoCargo.setAnoFim(2025);
    }

    @Test
    void testarAtualizarCargo() {
        // Simulando que o cargo existe no repositório
        when(cargoRepository.findById(1)).thenReturn(Optional.of(cargoExistente));
        when(cargoRepository.save(any(Cargo.class))).thenReturn(cargoExistente);

        // Chamando o método a ser testado
        Cargo cargoAtualizado = cargoService.atualizarCargo(1, novoCargo);

        // Verificando se o método save foi chamado e os campos foram atualizados corretamente
        assertNotNull(cargoAtualizado);
        assertEquals("Cargo Atualizado", cargoAtualizado.getDescricao());
        assertEquals("Novo Local", cargoAtualizado.getLocal());
        assertEquals(2023, cargoAtualizado.getAnoInicio());
        assertEquals(2025, cargoAtualizado.getAnoFim());

        verify(cargoRepository).findById(1);  // Verificando se o findById foi chamado
        verify(cargoRepository).save(cargoExistente);  // Verificando se o save foi chamado
    }

    @Test
    void testarAtualizarCargo_CargoNaoEncontrado() {
        // Simulando que o cargo não foi encontrado
        when(cargoRepository.findById(1)).thenReturn(Optional.empty());

        // Esperando que a exceção seja lançada
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cargoService.atualizarCargo(1, novoCargo);
        });

        assertEquals("Cargo com ID 1 não encontrado", exception.getMessage());
        verify(cargoRepository).findById(1);  // Verificando se o findById foi chamado
        verify(cargoRepository, never()).save(any(Cargo.class));  // Verificando se o save NÃO foi chamado
    }



    //deletar
    @BeforeEach
    void inicializarMocks() {
        MockitoAnnotations.openMocks(this);  // Inicializa o mock
        cargoService = new CargoService(cargoRepository,egressoRepository);  // Inicializa o serviço
    }

    @Test
    void testDeletarCargoComIdExistente() {
        // Arrange: configura o comportamento do mock
        Integer idCargo = 1;
        when(cargoRepository.existsById(idCargo)).thenReturn(true);

        // Act: chama o método a ser testado
        cargoService.deletarCargo(idCargo);

        // Assert: verifica se o método de deleção foi chamado no repositório
        verify(cargoRepository, times(1)).deleteById(idCargo);
    }

    @Test
    void testDeletarCargoComIdNaoExistente() {
        // Arrange: configura o comportamento do mock
        Integer idCargo = 999;
        when(cargoRepository.existsById(idCargo)).thenReturn(false);

        // Act & Assert: verifica se a exceção foi lançada
        RuntimeException exception = assertThrows(RuntimeException.class, () -> cargoService.deletarCargo(idCargo));
        assertEquals("Cargo com ID 999 não encontrado", exception.getMessage());

        // Verifica se o método de deleção nunca foi chamado
        verify(cargoRepository, never()).deleteById(idCargo);
    }
}


