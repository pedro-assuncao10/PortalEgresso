package com.example.demo.controller;

import com.example.demo.dto.DepoimentoRequisicaoDTO;
import com.example.demo.model.Depoimento;
import com.example.demo.service.DepoimentoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DepoimentoController.class)
@ExtendWith(MockitoExtension.class)
class DepoimentoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private DepoimentoService depoimentoService;

    @InjectMocks
    private DepoimentoController depoimentoController;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void listarTodosDepoimentos_DeveRetornarListaDeDepoimentos() throws Exception {
        List<Depoimento> depoimentos = Arrays.asList(new Depoimento(), new Depoimento());

        when(depoimentoService.listarTodosDepoimentos()).thenReturn(depoimentos);

        mockMvc.perform(get("/api/depoimentos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(depoimentoService, times(1)).listarTodosDepoimentos();
    }

    @Test
    void criarDepoimento_DeveRetornarDepoimentoCriado() throws Exception {
        DepoimentoRequisicaoDTO dto = new DepoimentoRequisicaoDTO();
        dto.setIdEgresso(1);
        dto.setTexto("Depoimento Teste");

        Depoimento depoimento = new Depoimento();
        depoimento.setIdDepoimento(1);
        depoimento.setTexto("Depoimento Teste");
        depoimento.setData(new Date());

        when(depoimentoService.criarDepoimento(eq(1), any(Depoimento.class))).thenReturn(depoimento);

        mockMvc.perform(post("/api/depoimentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idDepoimento").value(1))
                .andExpect(jsonPath("$.texto").value("Depoimento Teste"));

        verify(depoimentoService, times(1)).criarDepoimento(eq(1), any(Depoimento.class));
    }

    @Test
    void deletarDepoimento_DeveRetornarNoContent() throws Exception {
        DepoimentoRequisicaoDTO dto = new DepoimentoRequisicaoDTO();
        dto.setIdDepoimento(1);

        doNothing().when(depoimentoService).deletarDepoimento(1);

        mockMvc.perform(delete("/api/depoimentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNoContent());

        verify(depoimentoService, times(1)).deletarDepoimento(1);
    }

    @Test
    void buscarDepoimentoPorId_DeveRetornarDepoimento() throws Exception {
        DepoimentoRequisicaoDTO dto = new DepoimentoRequisicaoDTO();
        dto.setIdDepoimento(1);

        Depoimento depoimento = new Depoimento();
        depoimento.setIdDepoimento(1);
        depoimento.setTexto("Depoimento Encontrado");

        when(depoimentoService.buscarDepoimentoPorId(1)).thenReturn(depoimento);

        mockMvc.perform(post("/api/depoimentos/buscar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idDepoimento").value(1))
                .andExpect(jsonPath("$.texto").value("Depoimento Encontrado"));

        verify(depoimentoService, times(1)).buscarDepoimentoPorId(1);
    }

    @Test
    void atualizarDepoimento_DeveRetornarDepoimentoAtualizado() throws Exception {
        DepoimentoRequisicaoDTO dto = new DepoimentoRequisicaoDTO();
        dto.setIdDepoimento(1);
        dto.setTexto("Depoimento Atualizado");

        Depoimento depoimento = new Depoimento();
        depoimento.setIdDepoimento(1);
        depoimento.setTexto("Depoimento Atualizado");

        when(depoimentoService.atualizarDepoimento(eq(1), any(Depoimento.class))).thenReturn(depoimento);

        mockMvc.perform(put("/api/depoimentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idDepoimento").value(1))
                .andExpect(jsonPath("$.texto").value("Depoimento Atualizado"));

        verify(depoimentoService, times(1)).atualizarDepoimento(eq(1), any(Depoimento.class));
    }
}
