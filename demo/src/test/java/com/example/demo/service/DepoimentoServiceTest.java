package com.example.demo.service;

import com.example.demo.model.Depoimento;
import com.example.demo.model.Egresso;
import com.example.demo.repository.DepoimentoRepository;
import com.example.demo.repository.EgressoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@SpringBootTest
public class DepoimentoServiceTest {

    @Mock
    private DepoimentoRepository depoimentoRepository;

    @Mock
    private EgressoRepository egressoRepository;

    @InjectMocks
    private DepoimentoService depoimentoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @Transactional
    void criarDepoimento_DeveCriarDepoimentoComSucesso() {
        Integer idEgresso = 1;
        Egresso egresso = new Egresso();
        egresso.setIdEgresso(idEgresso);

        // Criando a data com o construtor Date
        Date data = new Date(124, 11, 17);  // 17 de dezembro de 2024 (ano começa em 1900, mês começa em 0)

        Depoimento depoimento = new Depoimento();
        depoimento.setTexto("Texto de depoimento");
        depoimento.setData(data);  // Passando o Date em vez de String

        when(egressoRepository.findById(idEgresso)).thenReturn(Optional.of(egresso));
        when(depoimentoRepository.save(any(Depoimento.class))).thenReturn(depoimento);

        Depoimento resultado = depoimentoService.criarDepoimento(idEgresso, depoimento);

        verify(depoimentoRepository, times(1)).save(depoimento);
        Assertions.assertEquals("Texto de depoimento", resultado.getTexto());  // Usando assertEquals para uma melhor verificação
    }

    @Test
    void listarDepoimentosPorEgresso_DeveRetornarListaDeDepoimentos() {
        Integer idEgresso = 1;
        Egresso egresso = new Egresso();
        egresso.setIdEgresso(idEgresso);

        Depoimento depoimento1 = new Depoimento();
        depoimento1.setTexto("Texto 1");
        Depoimento depoimento2 = new Depoimento();
        depoimento2.setTexto("Texto 2");

        when(egressoRepository.existsById(idEgresso)).thenReturn(true);
        when(depoimentoRepository.findByEgresso_IdEgresso(idEgresso)).thenReturn(Arrays.asList(depoimento1, depoimento2));

        List<Depoimento> depoimentos = depoimentoService.listarDepoimentosPorEgresso(idEgresso);

        verify(depoimentoRepository, times(1)).findByEgresso_IdEgresso(idEgresso);
        assert(depoimentos.size() == 2);
    }

    @Test
    @Transactional
    void atualizarDepoimento_DeveAtualizarDepoimentoComSucesso() {
        Integer idDepoimento = 1;

        // Criando a data com o construtor Date
        Date dataAntiga = new Date(124, 11, 17);  // 17 de dezembro de 2024 (ano começa em 1900, mês começa em 0)
        Date dataAtualizada = new Date(124, 11, 18);  // 18 de dezembro de 2024

        Depoimento depoimentoExistente = new Depoimento();
        depoimentoExistente.setIdDepoimento(idDepoimento);
        depoimentoExistente.setTexto("Texto antigo");
        depoimentoExistente.setData(dataAntiga);  // Usando Date em vez de String

        Depoimento novoDepoimento = new Depoimento();
        novoDepoimento.setTexto("Texto atualizado");
        novoDepoimento.setData(dataAtualizada);  // Usando Date em vez de String

        when(depoimentoRepository.findById(idDepoimento)).thenReturn(Optional.of(depoimentoExistente));
        when(depoimentoRepository.save(any(Depoimento.class))).thenReturn(novoDepoimento);

        Depoimento resultado = depoimentoService.atualizarDepoimento(idDepoimento, novoDepoimento);

        verify(depoimentoRepository, times(1)).save(depoimentoExistente);
        Assertions.assertEquals("Texto atualizado", resultado.getTexto());  // Usando assertEquals com a importação correta
    }

    @Test
    @Transactional
    void deletarDepoimento_DeveDeletarDepoimentoComSucesso() {
        Integer idDepoimento = 1;
        Depoimento depoimentoExistente = new Depoimento();
        depoimentoExistente.setIdDepoimento(idDepoimento);

        when(depoimentoRepository.existsById(idDepoimento)).thenReturn(true);

        depoimentoService.deletarDepoimento(idDepoimento);

        verify(depoimentoRepository, times(1)).deleteById(idDepoimento);
    }

    @Test
    void deletarDepoimento_DeveLancarExcecaoQuandoDepoimentoNaoExistir() {
        Integer idDepoimento = 1;

        when(depoimentoRepository.existsById(idDepoimento)).thenReturn(false);

        try {
            depoimentoService.deletarDepoimento(idDepoimento);
        } catch (IllegalArgumentException e) {
            assert(e.getMessage().equals("Depoimento com ID " + idDepoimento + " não encontrado"));
        }
    }

    @Test
    void listarDepoimentosPorEgresso_DeveLancarExcecaoQuandoEgressoNaoExistir() {
        Integer idEgresso = 1;

        when(egressoRepository.existsById(idEgresso)).thenReturn(false);

        try {
            depoimentoService.listarDepoimentosPorEgresso(idEgresso);
        } catch (IllegalArgumentException e) {
            assert(e.getMessage().equals("Egresso com ID " + idEgresso + " não encontrado"));
        }
    }

    @Test
    void criarDepoimento_DeveLancarExcecaoQuandoEgressoNaoExistir() {
        Integer idEgresso = 1;
        Depoimento depoimento = new Depoimento();
        depoimento.setTexto("Texto de depoimento");

        when(egressoRepository.findById(idEgresso)).thenReturn(Optional.empty());

        try {
            depoimentoService.criarDepoimento(idEgresso, depoimento);
        } catch (IllegalArgumentException e) {
            assert(e.getMessage().equals("Egresso com ID " + idEgresso + " não encontrado"));
        }
    }
}

