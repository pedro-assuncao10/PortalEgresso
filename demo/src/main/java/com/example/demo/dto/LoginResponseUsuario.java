package com.example.demo.dto;

import com.example.demo.model.Usuario;
import jakarta.validation.constraints.NotBlank;

public class LoginResponseUsuario {
    private String mensagem;
    private Usuario usuario;

    public LoginResponseUsuario(String mensagem, Usuario usuario) {
        this.mensagem = mensagem;
        this.usuario = usuario;
    }

    // Getters e Setters
    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
