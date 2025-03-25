import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.model.Coordenador;
import com.example.demo.repository.CoordenadorRepository;
import com.example.demo.service.CoordenadorService;
import com.example.demo.dto.CadastroRequest;
import com.example.demo.dto.CadastroResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;
import java.util.Optional;

public class CoordenadorServiceTest {

    @Mock
    private CoordenadorRepository repository;

    @InjectMocks
    private CoordenadorService coordenadorService;

    private Coordenador coordenador;
    private CadastroRequest cadastroRequest;
    private CadastroResponse cadastroResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Criando coordenador com dados fictícios
        coordenador = new Coordenador();
        coordenador.setIdCoordenador(1L);
        coordenador.setLogin("coordenador_login");
        coordenador.setSenha("senha123");
        coordenador.setTipo("coordenador");

        // Criando objeto CadastroRequest
        CadastroRequest cadastroRequest = new CadastroRequest();
        cadastroRequest.setNome("josé");
        cadastroRequest.setLogin("coordenador_novo");
        cadastroRequest.setTipo("coordenador");

        // Criando objeto CadastroResponse
        cadastroResponse = new CadastroResponse("Usuário cadastrado com sucesso!", coordenador.getIdCoordenador());
    }

    @Test
    void autenticar_DeveAutenticarComSucesso() {
        // Dado
        when(repository.findByLoginAndSenha("coordenador_login", "senha123")).thenReturn(coordenador);

        // Quando
        Coordenador resultado = coordenadorService.autenticar("coordenador_login", "senha123");

        // Então
        assertNotNull(resultado);
        assertEquals("coordenador_login", resultado.getLogin());
    }

    @Test
    void autenticar_DeveLancarExcecaoQuandoLoginOuSenhaInvalidos() {
        // Dado
        when(repository.findByLoginAndSenha("coordenador_login", "senhaErrada")).thenReturn(null);

        // Quando
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            coordenadorService.autenticar("coordenador_login", "senhaErrada");
        });

        // Então
        assertEquals("Login ou senha inválidos!", exception.getMessage());
    }

    @Test
    void cadastrar_DeveCadastrarCoordenadorComSucesso() {
        // Dado
        when(repository.save(any(Coordenador.class))).thenReturn(coordenador);

        // Quando
        CadastroResponse resultado = coordenadorService.cadastrar(cadastroRequest);

        // Então
        assertNotNull(resultado);
        assertEquals("Usuário cadastrado com sucesso!", resultado.getMensagem());
        assertEquals(1L, resultado.getIdCoordenador());
    }

    @Test
    void listarTodos_DeveRetornarListaDeCoordenadores() {
        // Dado
        List<Coordenador> coordenadores = Arrays.asList(coordenador);
        when(repository.findAll()).thenReturn(coordenadores);

        // Quando
        List<Coordenador> resultado = coordenadorService.listarTodos();

        // Então
        assertEquals(1, resultado.size());
        assertEquals("coordenador_login", resultado.get(0).getLogin());
    }

    @Test
    void buscarPorId_DeveRetornarCoordenadorQuandoEncontrado() {
        // Dado
        when(repository.findById(1L)).thenReturn(Optional.of(coordenador));

        // Quando
        Optional<Coordenador> resultado = coordenadorService.buscarPorId(1L);

        // Então
        assertTrue(resultado.isPresent());
        assertEquals("coordenador_login", resultado.get().getLogin());
    }

    @Test
    void buscarPorId_DeveRetornarVazioQuandoNaoEncontrado() {
        // Dado
        when(repository.findById(1L)).thenReturn(Optional.empty());

        // Quando
        Optional<Coordenador> resultado = coordenadorService.buscarPorId(1L);

        // Então
        assertFalse(resultado.isPresent());
    }

    @Test
    void excluir_DeveExcluirCoordenadorComSucesso() {
        // Dado
        doNothing().when(repository).deleteById(1L);

        // Quando
        coordenadorService.excluir(1L);

        // Então
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void buscarPorLogin_DeveRetornarCoordenadorQuandoEncontrado() {
        // Dado
        when(repository.fetchByLogin("coordenador_login")).thenReturn(coordenador);

        // Quando
        Coordenador resultado = coordenadorService.buscarPorLogin("coordenador_login");

        // Então
        assertNotNull(resultado);
        assertEquals("coordenador_login", resultado.getLogin());
    }

    @Test
    void buscarPorLogin_DeveLancarExcecaoQuandoNaoEncontrado() {
        // Dado
        when(repository.fetchByLogin("coordenador_inexistente")).thenReturn(null);

        // Quando
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            coordenadorService.buscarPorLogin("coordenador_inexistente");
        });

        // Então
        assertEquals("Coordenador com login coordenador_inexistente não encontrado!", exception.getMessage());
    }

    @Test
    void verificarPorLoginESenha_DeveRetornarCoordenadorComSucesso() {
        // Dado
        when(repository.findByLoginAndSenha("coordenador_login", "senha123")).thenReturn(coordenador);

        // Quando
        Coordenador resultado = coordenadorService.verificarPorLoginESenha("coordenador_login", "senha123");

        // Então
        assertNotNull(resultado);
        assertEquals("coordenador_login", resultado.getLogin());
    }
}

