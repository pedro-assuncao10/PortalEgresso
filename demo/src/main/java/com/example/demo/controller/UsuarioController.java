package com.example.demo.controller;

import com.example.demo.dto.LoginRequestUsuario;
import com.example.demo.dto.LoginResponseUsuario;
import com.example.demo.dto.CadastroRequestUsuario;
import com.example.demo.dto.CadastroResponseUsuario;

import com.example.demo.model.Usuario;
import com.example.demo.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseUsuario> login(@RequestBody LoginRequestUsuario loginRequestUsuario) {
        try {
            Usuario usuario = usuarioService.autenticarUsuario(loginRequestUsuario.getLogin(), loginRequestUsuario.getSenha());
            LoginResponseUsuario response = new LoginResponseUsuario("Login realizado com sucesso!", usuario);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(new LoginResponseUsuario("Credenciais inválidas.", null));
        }
    }

    @PostMapping("/cadastro")
    public ResponseEntity<CadastroResponseUsuario> cadastrarUsuario(@RequestBody CadastroRequestUsuario cadastroRequestUsuario) {
        try {
            CadastroResponseUsuario response = usuarioService.cadastrarUsuario(cadastroRequestUsuario);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(new CadastroResponseUsuario("Erro ao cadastrar usuário: " + e.getMessage(), null));
        }
    }
}
