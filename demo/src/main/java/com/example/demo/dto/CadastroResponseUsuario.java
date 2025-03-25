package com.example.demo.dto;

public class CadastroResponseUsuario {

    private String mensagem;
    private Long id;

    // Constructor
    public CadastroResponseUsuario(String mensagem, Long id) {
        this.mensagem = mensagem;
        this.id = id;
    }

    // Getters and Setters
    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
