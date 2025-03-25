import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.model.Curso;
import com.example.demo.model.Coordenador;
import com.example.demo.repository.CursoRepository;
import com.example.demo.repository.CoordenadorRepository;
import com.example.demo.service.CursoService;
import com.example.demo.service.CoordenadorService;
import com.example.demo.dto.CursoRequest;
import com.example.demo.dto.CursoResponse;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.*;
import java.util.*;
import java.util.stream.Collectors;

public class CursoServiceTest {

    @Mock
    private CursoRepository cursoRepository;

    @Mock
    private CoordenadorRepository coordenadorRepository;

    @InjectMocks
    private CursoService cursoService;

    private Curso curso;
    private CursoRequest cursoRequest;
    private Coordenador coordenador;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Criando coordenador com dados fictícios
        coordenador = new Coordenador();
        coordenador.setIdCoordenador(1L);  // ID do coordenador
        coordenador.setLogin("coordenador_login");
        coordenador.setSenha("senha123");
        coordenador.setTipo("coordenador");

        // Criando o curso com dados fictícios
        curso = new Curso();
        curso.setIdCurso(1L);
        curso.setNome("Curso Teste");
        curso.setNivel("Básico");
        curso.setCoordenador(coordenador);

        // Criando o objeto CursoRequest
        cursoRequest = new CursoRequest();
        cursoRequest.setNome("Curso Teste Atualizado");
        cursoRequest.setNivel("Avançado");
        cursoRequest.setIdCoordenador(1L);  // ID do coordenador
    }

    @Test
    void listarCursos_DeveRetornarListaDeCursos() {
        // Dado
        List<Curso> cursos = Arrays.asList(curso);
        when(cursoRepository.findAll()).thenReturn(cursos);

        // Quando
        List<CursoResponse> resultado = cursoService.listarCursos();

        // Então
        assertEquals(1, resultado.size());
        assertEquals("Curso Teste", resultado.get(0).getNome());
    }

    @Test
    void cadastrarCurso_DeveCriarCursoComSucesso() {
        // Dado
        when(coordenadorRepository.findById(cursoRequest.getIdCoordenador())).thenReturn(Optional.of(coordenador));
        when(cursoRepository.save(any(Curso.class))).thenReturn(curso);

        // Quando
        cursoService.cadastrarCurso(cursoRequest);

        // Então
        verify(cursoRepository, times(1)).save(any(Curso.class));
    }

    @Test
    void buscarCursoPorId_DeveRetornarCursoComSucesso() {
        // Dado
        when(cursoRepository.findById(1L)).thenReturn(Optional.of(curso));

        // Quando
        CursoResponse resultado = cursoService.buscarCursoPorId(1L);

        // Então
        assertNotNull(resultado);
        assertEquals("Curso Teste", resultado.getNome());
    }

    @Test
    void atualizarCurso_DeveAtualizarCursoComSucesso() {
        // Dado
        when(cursoRepository.findById(1L)).thenReturn(Optional.of(curso));
        when(coordenadorRepository.findById(cursoRequest.getIdCoordenador())).thenReturn(Optional.of(coordenador));
        when(cursoRepository.save(any(Curso.class))).thenReturn(curso);

        // Quando
        CursoResponse resultado = cursoService.atualizarCurso(1L, cursoRequest);

        // Então
        assertNotNull(resultado);
        assertEquals("Curso Teste Atualizado", resultado.getNome());
    }

    @Test
    void deletarCurso_DeveDeletarCursoComSucesso() {
        // Dado
        when(cursoRepository.findById(1L)).thenReturn(Optional.of(curso));

        // Quando
        cursoService.deletarCurso(1L);

        // Então
        verify(cursoRepository, times(1)).delete(curso);
    }
}
