import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.model.Curso;
import com.example.demo.model.Egresso;
import com.example.demo.model.CursoEgresso;
import com.example.demo.repository.CursoRepository;
import com.example.demo.repository.EgressoRepository;
import com.example.demo.service.CursoService;
import com.example.demo.service.EgressoService;
import com.example.demo.repository.CursoEgressoRepository;
import com.example.demo.service.CursoEgressoService;
import com.example.demo.model.Coordenador;
import com.example.demo.repository.CoordenadorRepository;
import com.example.demo.service.CoordenadorService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

public class CursoEgressoServiceTest {

    @Mock
    private CursoEgressoRepository repository;

    @InjectMocks
    private CursoEgressoService cursoEgressoService;

    private CursoEgresso cursoEgresso;
    private Egresso egresso;
    private Curso curso;
    private Coordenador coordenador;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Criando objetos fictícios para Egresso e Coordenador
        egresso = new Egresso();
        egresso.setIdEgresso(1);
        egresso.setNome("Egresso 1");
        egresso.setEmail("egresso1@dominio.com");
        egresso.setDescricao("Descrição do Egresso 1");
        egresso.setFoto("foto1.jpg");
        egresso.setLinkedin("linkedin.com/in/egresso1");
        egresso.setInstagam("instagram.com/egresso1");
        egresso.setCurriculo("curriculo1.pdf");

        // Criando objeto Coordenador
        coordenador = new Coordenador();
        coordenador.setIdCoordenador(1L);
        coordenador.setLogin("coordenador1");
        coordenador.setSenha("senha123");
        coordenador.setTipo("egresso");

        // Criando objeto Curso
        curso = new Curso();
        curso.setIdCurso(1L);
        curso.setNome("Curso 1");
        curso.setNivel("Pós-Graduação");
        curso.setCoordenador(coordenador);

        // Criando objeto CursoEgresso
        cursoEgresso = new CursoEgresso();
        cursoEgresso.setIdCursoEgresso(1);
        cursoEgresso.setCurso(curso);
        cursoEgresso.setEgresso(egresso);
        cursoEgresso.setAnoInicio(2020);
        cursoEgresso.setAnoFim(2022);
    }

    @Test
    void listarTodos_DeveRetornarListaDeCursoEgresso() {
        // Dado
        List<CursoEgresso> lista = Arrays.asList(cursoEgresso);
        when(repository.findAll()).thenReturn(lista);

        // Quando
        List<CursoEgresso> resultado = cursoEgressoService.listarTodos();

        // Então
        assertEquals(1, resultado.size());
        assertEquals(1, resultado.get(0).getIdCursoEgresso());
        assertEquals("Curso 1", resultado.get(0).getCurso().getNome());
        assertEquals("Coordenador 1", resultado.get(0).getCurso().getCoordenador().getLogin());
    }

    @Test
    void buscarPorId_DeveRetornarCursoEgressoQuandoEncontrado() {
        // Dado
        when(repository.findById(1)).thenReturn(Optional.of(cursoEgresso));

        // Quando
        Optional<CursoEgresso> resultado = cursoEgressoService.buscarPorId(1);

        // Então
        assertTrue(resultado.isPresent());
        assertEquals(1, resultado.get().getIdCursoEgresso());
        assertEquals("Curso 1", resultado.get().getCurso().getNome());
        assertEquals("coordenador1", resultado.get().getCurso().getCoordenador().getLogin());
    }

    @Test
    void buscarPorId_DeveRetornarVazioQuandoNaoEncontrado() {
        // Dado
        when(repository.findById(1)).thenReturn(Optional.empty());

        // Quando
        Optional<CursoEgresso> resultado = cursoEgressoService.buscarPorId(1);

        // Então
        assertFalse(resultado.isPresent());
    }

    @Test
    void salvar_DeveSalvarCursoEgressoComSucesso() {
        // Dado
        when(repository.save(any(CursoEgresso.class))).thenReturn(cursoEgresso);

        // Quando
        CursoEgresso resultado = cursoEgressoService.salvar(cursoEgresso);

        // Então
        assertNotNull(resultado);
        assertEquals(1, resultado.getIdCursoEgresso());
        assertEquals("Curso 1", resultado.getCurso().getNome());
        assertEquals("coordenador1", resultado.getCurso().getCoordenador().getLogin());
    }

    @Test
    void excluir_DeveExcluirCursoEgressoComSucesso() {
        // Dado
        doNothing().when(repository).deleteById(1);

        // Quando
        cursoEgressoService.excluir(1);

        // Então
        verify(repository, times(1)).deleteById(1);
    }

    @Test
    void buscarPorIdCursoEgresso_DeveRetornarListaDeCursoEgresso() {
        // Dado
        List<CursoEgresso> lista = Arrays.asList(cursoEgresso);
        when(repository.findByIdCursoEgresso(1)).thenReturn(lista);

        // Quando
        List<CursoEgresso> resultado = cursoEgressoService.buscarPorIdCursoEgresso(1);

        // Então
        assertEquals(1, resultado.size());
        assertEquals(1, resultado.get(0).getIdCursoEgresso());
    }

    @Test
    void buscarPorEgresso_DeveRetornarListaDeCursoEgresso() {
        // Dado
        List<CursoEgresso> lista = Arrays.asList(cursoEgresso);
        when(repository.findByEgresso(egresso)).thenReturn(lista);

        // Quando
        List<CursoEgresso> resultado = cursoEgressoService.buscarPorEgresso(egresso);

        // Então
        assertEquals(1, resultado.size());
        assertEquals("Egresso 1", resultado.get(0).getEgresso().getNome());
        assertEquals("egresso1@dominio.com", resultado.get(0).getEgresso().getEmail());
        assertEquals("linkedin.com/in/egresso1", resultado.get(0).getEgresso().getLinkedin());
    }

    @Test
    void buscarPorCurso_DeveRetornarListaDeCursoEgresso() {
        // Dado
        List<CursoEgresso> lista = Arrays.asList(cursoEgresso);
        when(repository.findByCurso(curso)).thenReturn(lista);

        // Quando
        List<CursoEgresso> resultado = cursoEgressoService.buscarPorCurso(curso);

        // Então
        assertEquals(1, resultado.size());
        assertEquals("Curso 1", resultado.get(0).getCurso().getNome());
        assertEquals("coordenador1", resultado.get(0).getCurso().getCoordenador().getLogin());
    }

    @Test
    void buscarPorAnoInicio_DeveRetornarListaDeCursoEgresso() {
        // Dado
        List<CursoEgresso> lista = Arrays.asList(cursoEgresso);
        when(repository.findByAnoInicio(2020)).thenReturn(lista);

        // Quando
        List<CursoEgresso> resultado = cursoEgressoService.buscarPorAnoInicio(2020);

        // Então
        assertEquals(1, resultado.size());
        assertEquals(2020, resultado.get(0).getAnoInicio());
    }

    @Test
    void buscarPorAnoInicioEAnoFim_DeveRetornarListaDeCursoEgresso() {
        // Dado
        List<CursoEgresso> lista = Arrays.asList(cursoEgresso);
        when(repository.findByAnoInicioAndAnoFim(2020, 2022)).thenReturn(lista);

        // Quando
        List<CursoEgresso> resultado = cursoEgressoService.buscarPorAnoInicioEAnoFim(2020, 2022);

        // Então
        assertEquals(1, resultado.size());
        assertEquals(2020, resultado.get(0).getAnoInicio());
        assertEquals(2022, resultado.get(0).getAnoFim());
    }
}
