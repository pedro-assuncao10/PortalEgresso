package com.example.demo.controller;

import com.example.demo.dto.CargoRequisicaoDTO;
import com.example.demo.model.Cargo;
import com.example.demo.service.CargoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CargoController.class)
class CargoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CargoService cargoService;

    @Autowired
    private ObjectMapper objectMapper;

    private CargoRequisicaoDTO cargoRequisicaoDTO;
    private Cargo cargo;

    @BeforeEach
    void setUp() {
        cargoRequisicaoDTO = new CargoRequisicaoDTO();
        cargoRequisicaoDTO.setDescricao("Gerente de Projetos");
        cargoRequisicaoDTO.setLocal("São Paulo");
        cargoRequisicaoDTO.setAnoInicio(2015);
        cargoRequisicaoDTO.setAnoFim(2020);
        cargoRequisicaoDTO.setIdEgresso(1);
        cargoRequisicaoDTO.setIdCargo(1);

        cargo = new Cargo();
        cargo.setIdCargo(1);
        cargo.setDescricao("Gerente de Projetos");
        cargo.setLocal("São Paulo");
        cargo.setAnoInicio(2015);
        cargo.setAnoFim(2020);
    }

    @Test
    void criarCargo_DeveRetornarCargoCriado() throws Exception {
        Mockito.when(cargoService.criarCargo(eq(1), any(Cargo.class))).thenReturn(cargo);

        mockMvc.perform(post("/api/cargos/criar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cargoRequisicaoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCargo").value(1))
                .andExpect(jsonPath("$.descricao").value("Gerente de Projetos"))
                .andExpect(jsonPath("$.local").value("São Paulo"))
                .andExpect(jsonPath("$.anoInicio").value(2015))
                .andExpect(jsonPath("$.anoFim").value(2020));
    }

    @Test
    void buscarTodosOsCargos_DeveRetornarListaDeCargos() throws Exception {
        Mockito.when(cargoService.buscarTodosOsCargos()).thenReturn(Collections.singletonList(cargo));

        mockMvc.perform(get("/api/cargos/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].idCargo").value(1))
                .andExpect(jsonPath("$[0].descricao").value("Gerente de Projetos"));
    }

    @Test
    void atualizarCargo_DeveRetornarCargoAtualizado() throws Exception {
        Mockito.when(cargoService.atualizarCargo(eq(1), any(Cargo.class))).thenReturn(cargo);

        mockMvc.perform(put("/api/cargos/atualizar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cargoRequisicaoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCargo").value(1))
                .andExpect(jsonPath("$.descricao").value("Gerente de Projetos"))
                .andExpect(jsonPath("$.local").value("São Paulo"));
    }

    @Test
    void deletarCargo_DeveRetornarMensagemDeSucesso() throws Exception {
        Mockito.doNothing().when(cargoService).deletarCargo(1);

        mockMvc.perform(delete("/api/cargos/deletar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cargoRequisicaoDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("Cargo deletado com sucesso!"));
    }
}

