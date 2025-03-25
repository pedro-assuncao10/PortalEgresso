package com.example.demo.controller;

import com.example.demo.dto.EgressoDTO;
import com.example.demo.model.Egresso;
import com.example.demo.service.EgressoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EgressoController.class)
class EgressoControllerErrorTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EgressoService egressoService;

    @Autowired
    private ObjectMapper objectMapper;

    private EgressoDTO egressoDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        egressoDTO = new EgressoDTO();
        egressoDTO.setNome(""); // Nome inválido (vazio)
        egressoDTO.setEmail("email-invalido"); // Email inválido
    }

    @Test
    void testCriarEgressoDadosInvalidos() throws Exception {
        mockMvc.perform(post("/api/egressos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(egressoDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testBuscarEgressoInexistente() throws Exception {
        when(egressoService.buscarEgressoPorId(999)).thenReturn(null);

        mockMvc.perform(get("/api/egressos/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testEditarEgressoInexistente() throws Exception {
        when(egressoService.editarEgresso(any(Integer.class), any(Egresso.class), any(Long.class), any(Integer.class), any(Integer.class)))
                .thenThrow(new RuntimeException("Egresso não encontrado"));

        mockMvc.perform(put("/api/egressos/999")
                        .param("idCurso", "1")
                        .param("anoInicio", "2015")
                        .param("anoFim", "2019")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(egressoDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testExcluirEgressoInexistente() throws Exception {
        doThrow(new RuntimeException("Egresso não encontrado")).when(egressoService).excluirEgresso(999);

        mockMvc.perform(delete("/api/egressos/999"))
                .andExpect(status().isNotFound());
    }
}
