package com.example.demo.service;

import com.example.demo.dto.EgressoDTO;
import com.example.demo.model.Curso;
import com.example.demo.model.CursoEgresso;
import com.example.demo.model.Egresso;
import com.example.demo.repository.CursoEgressoRepository;
import com.example.demo.repository.CursoRepository;
import com.example.demo.repository.EgressoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EgressoServiceTest {

    @InjectMocks
    private EgressoService egressoService;

    @Mock
    private EgressoRepository egressoRepository;

    @Mock
    private CursoRepository cursoRepository;

    @Mock
    private CursoEgressoRepository cursoEgressoRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCriarEgresso() {
        // Mock dos dados
        EgressoDTO egressoDTO = new EgressoDTO();
        egressoDTO.setNome("John Doe");
        egressoDTO.setEmail("john.doe@example.com");
        egressoDTO.setDescricao("Egresso de engenharia");
        egressoDTO.setFoto("foto.jpg");
        egressoDTO.setLinkedin("linkedin.com/in/johndoe");
        egressoDTO.setInstagam("instagram.com/johndoe");
        egressoDTO.setCurriculo("Currículo detalhado");
        egressoDTO.setIdCurso(1L);
        egressoDTO.setAnoInicio(2015);
        egressoDTO.setAnoFim(2019);

        Curso curso = new Curso();
        curso.setIdCurso(1L);

        Egresso egressoSalvo = new Egresso();
        egressoSalvo.setIdEgresso(1);
        egressoSalvo.setNome(egressoDTO.getNome());
        egressoSalvo.setEmail(egressoDTO.getEmail());
        egressoSalvo.setDescricao(egressoDTO.getDescricao());
        egressoSalvo.setFoto(egressoDTO.getFoto());
        egressoSalvo.setLinkedin(egressoDTO.getLinkedin());
        egressoSalvo.setInstagam(egressoDTO.getInstagam());
        egressoSalvo.setCurriculo(egressoDTO.getCurriculo());

        when(cursoRepository.findById(1L)).thenReturn(Optional.of(curso));
        when(egressoRepository.save(any(Egresso.class))).thenReturn(egressoSalvo);

        // Execução
        Egresso result = egressoService.criarEgresso(egressoDTO);

        // Verificação
        assertNotNull(result);
        assertEquals("John Doe", result.getNome());
        assertEquals("john.doe@example.com", result.getEmail());
        verify(egressoRepository, times(1)).save(any(Egresso.class));
        verify(cursoEgressoRepository, times(1)).save(any(CursoEgresso.class));
    }

    @Test
    void testListarEgressos() {
        // Mock dos dados
        Egresso egresso1 = new Egresso();
        egresso1.setIdEgresso(1);
        egresso1.setNome("John Doe");

        Egresso egresso2 = new Egresso();
        egresso2.setIdEgresso(2);
        egresso2.setNome("Jane Smith");

        when(egressoRepository.findAll()).thenReturn(Arrays.asList(egresso1, egresso2));

        // Execução
        List<Egresso> result = egressoService.listarEgressos();

        // Verificação
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("John Doe", result.get(0).getNome());
        assertEquals("Jane Smith", result.get(1).getNome());
        verify(egressoRepository, times(1)).findAll();
    }

    @Test
    void testBuscarEgressoPorId() {
        // Mock dos dados
        Egresso egresso = new Egresso();
        egresso.setIdEgresso(1);
        egresso.setNome("John Doe");

        when(egressoRepository.findById(1)).thenReturn(Optional.of(egresso));

        // Execução
        Egresso result = egressoService.buscarEgressoPorId(1);

        // Verificação
        assertNotNull(result);
        assertEquals("John Doe", result.getNome());
        verify(egressoRepository, times(1)).findById(1);
    }

    @Test
    void testEditarEgresso() {
        // Mock dos dados
        Egresso egressoExistente = new Egresso();
        egressoExistente.setIdEgresso(1);
        egressoExistente.setNome("John Doe");

        Curso curso = new Curso();
        curso.setIdCurso(1L);

        Egresso egressoAtualizado = new Egresso();
        egressoAtualizado.setNome("Jane Doe");

        when(egressoRepository.findById(1)).thenReturn(Optional.of(egressoExistente));
        when(cursoRepository.findById(1L)).thenReturn(Optional.of(curso));
        when(egressoRepository.save(any(Egresso.class))).thenReturn(egressoAtualizado);

        // Execução
        Egresso result = egressoService.editarEgresso(1, egressoAtualizado, 1L, 2016, 2020);

        // Verificação
        assertNotNull(result);
        assertEquals("Jane Doe", result.getNome());
        verify(egressoRepository, times(1)).findById(1);
        verify(cursoEgressoRepository, times(1)).save(any(CursoEgresso.class));
    }

    @Test
    void testExcluirEgresso() {
        // Mock dos dados
        Egresso egresso = new Egresso();
        egresso.setIdEgresso(1);

        when(egressoRepository.findById(1)).thenReturn(Optional.of(egresso));

        // Execução
        egressoService.excluirEgresso(1);

        // Verificação
        verify(egressoRepository, times(1)).findById(1);
        verify(cursoEgressoRepository, times(1)).deleteByEgresso(any(Egresso.class));
        verify(egressoRepository, times(1)).delete(any(Egresso.class));
    }

    @Test
    void testObterEgressosPorCursoEAno() {
        // Mock dos dados
        Egresso egresso = new Egresso();
        egresso.setIdEgresso(1);
        egresso.setNome("John Doe");

        when(cursoEgressoRepository.findEgressosByCursoAndAno(1, 2015, 2019))
                .thenReturn(Arrays.asList(egresso));

        // Execução
        List<Egresso> result = egressoService.obterEgressosPorCursoEAno(1, 2015, 2019);

        // Verificação
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getNome());
        verify(cursoEgressoRepository, times(1)).findEgressosByCursoAndAno(1, 2015, 2019);
    }
}
