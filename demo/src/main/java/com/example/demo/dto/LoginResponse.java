package com.example.demo.dto;

import com.example.demo.model.Coordenador;

public class LoginResponse {
    private String mensagem;
    private Coordenador coordenador;

    public LoginResponse(String mensagem, Coordenador coordenador) {
        this.mensagem = mensagem;
        this.coordenador = coordenador;
    }

    // Getters e Setters
    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public Coordenador getCoordenador() {
        return coordenador;
    }

    public void setCoordenador(Coordenador coordenador) {
        this.coordenador = coordenador;
    }
}
