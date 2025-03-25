package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CursoRequest {

    @NotBlank(message = "O nome do curso é obrigatório.")
    private String nome;

    @NotBlank(message = "O nível do curso é obrigatório.")
    private String nivel;

    private Long idCoordenador;

    // Getters e Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public Long getIdCoordenador() {
        return idCoordenador;
    }

    public void setIdCoordenador(Long idCoordenador) {
        this.idCoordenador = idCoordenador;
    }
}
