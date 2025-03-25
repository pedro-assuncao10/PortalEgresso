package com.example.demo.controller;

import com.example.demo.dto.EgressoDTO;
import com.example.demo.model.Curso;
import com.example.demo.model.Egresso;
import com.example.demo.service.EgressoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EgressoController.class)
class EgressoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EgressoService egressoService;

    @Autowired
    private ObjectMapper objectMapper;

    private EgressoDTO egressoDTO;
    private Egresso egresso;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        egressoDTO = new EgressoDTO();
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

        egresso = new Egresso();
        egresso.setIdEgresso(1);
        egresso.setNome(egressoDTO.getNome());
        egresso.setEmail(egressoDTO.getEmail());
        egresso.setDescricao(egressoDTO.getDescricao());
        egresso.setFoto(egressoDTO.getFoto());
        egresso.setLinkedin(egressoDTO.getLinkedin());
        egresso.setInstagam(egressoDTO.getInstagam());
        egresso.setCurriculo(egressoDTO.getCurriculo());
    }

    @Test
    void testCriarEgresso() throws Exception {
        when(egressoService.criarEgresso(any(EgressoDTO.class))).thenReturn(egresso);

        mockMvc.perform(post("/api/egressos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(egressoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }

    @Test
    void testListarEgressos() throws Exception {
        when(egressoService.listarEgressos()).thenReturn(Arrays.asList(egresso));

        mockMvc.perform(get("/api/egressos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("John Doe"));
    }

    @Test
    void testBuscarEgressoPorId() throws Exception {
        when(egressoService.buscarEgressoPorId(1)).thenReturn(egresso);

        mockMvc.perform(get("/api/egressos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("John Doe"));
    }

    @Test
    void testEditarEgresso() throws Exception {
        Egresso egressoAtualizado = new Egresso();
        egressoAtualizado.setIdEgresso(1);
        egressoAtualizado.setNome("Jane Doe");

        when(egressoService.editarEgresso(any(Integer.class), any(Egresso.class), any(Long.class), any(Integer.class), any(Integer.class)))
                .thenReturn(egressoAtualizado);

        mockMvc.perform(put("/api/egressos/1")
                        .param("idCurso", "1")
                        .param("anoInicio", "2015")
                        .param("anoFim", "2019")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(egressoAtualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Jane Doe"));
    }

    @Test
    void testExcluirEgresso() throws Exception {
        mockMvc.perform(delete("/api/egressos/1"))
                .andExpect(status().isNoContent());
    }
}
