package com.example.demo.dto;

public class CadastroResponse {

    private String mensagem;
    private Long idCoordenador;

    // Constructor
    public CadastroResponse(String mensagem, Long idCoordenador) {
        this.mensagem = mensagem;
        this.idCoordenador = idCoordenador;
    }

    // Getters and Setters
    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public Long getIdCoordenador() {
        return idCoordenador;
    }

    public void setIdCoordenador(Long idCoordenador) {
        this.idCoordenador = idCoordenador;
    }
}
