import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.demo.controller.CursoController;


import com.example.demo.dto.CursoRequest;
import com.example.demo.dto.CursoResponse;
import com.example.demo.service.CursoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class CursoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CursoService cursoService;

    @InjectMocks
    private CursoController cursoController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(cursoController).build();
    }

    @Test
    void listarCursos_DeveRetornarListaDeCursos() throws Exception {
        when(cursoService.listarCursos()).thenReturn(Collections.singletonList(new CursoResponse()));

        mockMvc.perform(get("/api/cursos"))
                .andExpect(status().isOk());
    }

    @Test
    void buscarCursoPorId_DeveRetornarCurso_SeEncontrado() throws Exception {
        Long idCurso = 1L;
        CursoResponse cursoResponse = new CursoResponse();
        when(cursoService.buscarCursoPorId(idCurso)).thenReturn(cursoResponse);

        Map<String, Long> request = new HashMap<>();
        request.put("idCurso", idCurso);

        mockMvc.perform(post("/api/cursos/buscar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void cadastrarCurso_DeveRetornarSucesso_SeCursoForCadastrado() throws Exception {
        CursoRequest cursoRequest = new CursoRequest();
        cursoRequest.setNome("Engenharia");
        cursoRequest.setNivel("Superior");

        CursoResponse cursoResponse = new CursoResponse();
        when(cursoService.buscarCursoPorNome(cursoRequest.getNome())).thenReturn(cursoResponse);

        mockMvc.perform(post("/api/cursos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cursoRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    void atualizarCurso_DeveRetornarSucesso_SeCursoForAtualizado() throws Exception {
        Long idCurso = 1L;
        CursoRequest cursoRequest = new CursoRequest();
        cursoRequest.setNome("Engenharia Atualizado");
        cursoRequest.setNivel("Superior");

        Map<String, Object> request = new HashMap<>();
        request.put("idCurso", idCurso);
        request.put("nome", cursoRequest.getNome());
        request.put("nivel", cursoRequest.getNivel());

        when(cursoService.atualizarCurso(eq(idCurso), any())).thenReturn(new CursoResponse());

        mockMvc.perform(put("/api/cursos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void deletarCurso_DeveRetornarSucesso_SeCursoForDeletado() throws Exception {
        Long idCurso = 1L;
        Map<String, Long> request = new HashMap<>();
        request.put("idCurso", idCurso);

        doNothing().when(cursoService).deletarCurso(idCurso);

        mockMvc.perform(delete("/api/cursos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }
}
