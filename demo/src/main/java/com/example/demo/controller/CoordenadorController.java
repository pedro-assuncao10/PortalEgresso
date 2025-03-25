package com.example.demo.controller;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.LoginResponse;
import com.example.demo.dto.CadastroRequest;
import com.example.demo.dto.CadastroResponse;
import com.example.demo.security.JwtUtil;

import com.example.demo.model.Coordenador;
import com.example.demo.service.CoordenadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/coordenadores")
public class CoordenadorController {

    @Autowired
    private CoordenadorService coordenadorService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Coordenador coordenador = coordenadorService.autenticar(loginRequest.getLogin(), loginRequest.getSenha());

            // ✅ Gera um token JWT para o usuário autenticado
            String token = jwtUtil.generateToken(coordenador.getLogin());

            // ✅ Retorna o token + objeto completo do Coordenador
            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "coordenador", coordenador // 🔥 Agora o frontend recebe o objeto inteiro
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(Map.of("error", "Credenciais inválidas."));
        }
    }




    @PostMapping("/cadastro")
    public ResponseEntity<CadastroResponse> cadastrar(@RequestBody CadastroRequest cadastroRequest) {
        try {
            CadastroResponse response = coordenadorService.cadastrar(cadastroRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(new CadastroResponse("Erro ao cadastrar usuário: " + e.getMessage(), null));
        }
    }

    @GetMapping("/buscar/{login}")
    public ResponseEntity<?> buscarCoordenadorPorLogin(@PathVariable String login) {
        try {
            Coordenador coordenador = coordenadorService.buscarPorLogin(login);
            return ResponseEntity.ok(coordenador);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body("Erro: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro interno no servidor");
        }
    }
}

