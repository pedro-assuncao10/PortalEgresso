package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Mensagem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String conteudo;

    @ManyToOne
    private Egresso remetente;

    @ManyToOne
    @JsonIgnore
    private GrupoDiscussao grupo;

    private LocalDateTime dataEnvio;


    //getters e settters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public Egresso getRemetente() {
        return remetente;
    }

    public void setRemetente(Egresso remetente) {
        this.remetente = remetente;
    }

    public GrupoDiscussao getGrupo() {
        return grupo;
    }

    public void setGrupo(GrupoDiscussao grupo) {
        this.grupo = grupo;
    }

    public LocalDateTime getDataEnvio() {
        return dataEnvio;
    }

    public void setDataEnvio(LocalDateTime dataEnvio) {
        this.dataEnvio = dataEnvio;
    }
}
