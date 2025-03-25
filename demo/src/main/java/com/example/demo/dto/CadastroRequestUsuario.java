package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;

public class CadastroRequestUsuario {

    @NotBlank(message = "O campo 'nome' é obrigatório.")
    private String nome;

    @NotBlank(message = "O campo 'login' é obrigatório.")
    private String login;

    @NotBlank(message = "O campo 'senha' é obrigatório.")
    private String senha;

    @NotBlank(message = "O campo 'tipo' é obrigatorio.")
    private String Tipo;

    // Getters and Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public @NotBlank(message = "O campo 'tipo' é obrigatorio.") String getTipo() {
        return Tipo;
    }

    public void setTipo(@NotBlank(message = "O campo 'tipo' é obrigatorio.") String tipo) {
        Tipo = tipo;
    }
}

