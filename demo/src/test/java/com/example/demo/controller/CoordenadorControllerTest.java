package com.example.demo.controller;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.CadastroRequest;
import com.example.demo.dto.CadastroResponse;
import com.example.demo.model.Coordenador;
import com.example.demo.service.CoordenadorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CoordenadorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private CoordenadorService coordenadorService;

    @InjectMocks
    private CoordenadorController coordenadorController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(coordenadorController).build();
    }

    // Teste para login com credenciais válidas
    @Test
    void login_DeveRetornarSucesso_SeCredenciaisForemValidas() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setLogin("admin");
        loginRequest.setSenha("12345");

        Coordenador coordenador = new Coordenador();
        coordenador.setLogin("admin");
        coordenador.setSenha("12345");
        coordenador.setTipo("egresso");

        when(coordenadorService.autenticar("admin", "12345")).thenReturn(coordenador);

        mockMvc.perform(post("/api/coordenadores/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\":\"admin\", \"senha\":\"12345\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensagem").value("Login realizado com sucesso!"))
                .andExpect(jsonPath("$.coordenador.login").value("admin"));
    }

    // Teste para login com credenciais inválidas
    @Test
    void login_DeveRetornarErro_SeCredenciaisForemInvalidas() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setLogin("usuarioInvalido");
        loginRequest.setSenha("senhaErrada");

        when(coordenadorService.autenticar("usuarioInvalido", "senhaErrada"))
                .thenThrow(new RuntimeException("Credenciais inválidas"));

        mockMvc.perform(post("/api/coordenadores/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\":\"usuarioInvalido\", \"senha\":\"senhaErrada\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.mensagem").value("Credenciais inválidas."));
    }

    // Teste para cadastro com sucesso
    @Test
    void cadastro_DeveRetornarSucesso_SeCadastroForValido() throws Exception {
        CadastroRequest cadastroRequest = new CadastroRequest();
        cadastroRequest.setLogin("novoUsuario");
        cadastroRequest.setSenha("novaSenha");
        cadastroRequest.setTipo("egresso");

        CadastroResponse cadastroResponse = new CadastroResponse("Cadastro realizado com sucesso!", 1L);

        when(coordenadorService.cadastrar(cadastroRequest)).thenReturn(cadastroResponse);

        mockMvc.perform(post("/api/coordenadores/cadastro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\":\"novoUsuario\", \"senha\":\"novaSenha\", \"tipo\":\"egresso\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensagem").value("Cadastro realizado com sucesso!"))
                .andExpect(jsonPath("$.id").value(1L));
    }

    // Teste para cadastro com erro
    @Test
    void cadastro_DeveRetornarErro_SeCadastroForInvalido() throws Exception {
        CadastroRequest cadastroRequest = new CadastroRequest();
        cadastroRequest.setLogin("usuarioExistente");
        cadastroRequest.setSenha("senhaExistente");

        when(coordenadorService.cadastrar(cadastroRequest))
                .thenThrow(new RuntimeException("Erro ao cadastrar usuário"));

        mockMvc.perform(post("/api/coordenadores/cadastro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\":\"usuarioExistente\", \"senha\":\"senhaExistente\"}"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.mensagem").value("Erro ao cadastrar usuário: Erro ao cadastrar usuário"));
    }
}
