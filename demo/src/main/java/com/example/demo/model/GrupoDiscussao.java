package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import com.example.demo.model.Mensagem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class GrupoDiscussao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @ManyToOne
    private Egresso criador;

    @ManyToMany
    private List<Egresso> participantes;

    @OneToMany(mappedBy = "grupo", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Mensagem> mensagens;

    //getters e setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Egresso getCriador() {
        return criador;
    }

    public void setCriador(Egresso criador) {
        this.criador = criador;
    }

    public List<Egresso> getParticipantes() {
        return participantes;
    }

    public void setParticipantes(List<Egresso> participantes) {
        this.participantes = participantes;
    }

    public List<Mensagem> getMensagens() {
        return mensagens;
    }

    public void setMensagens(List<Mensagem> mensagens) {
        this.mensagens = mensagens;
    }
}
