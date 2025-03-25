package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.persistence.Entity;

@Entity
@Table(name = "egresso")
public class Egresso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_egresso", nullable = false)
    private Integer idEgresso;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "foto")
    private String foto;

    @Column(name = "linkedin")
    private String linkedin;

    @Column(name = "instagam")
    private String instagam;

    @Column(name = "curriculo")
    private String curriculo;

    //getters and setters
    public Integer getIdEgresso() {
        return idEgresso;
    }

    public void setIdEgresso(Integer idEgresso) {
        this.idEgresso = idEgresso;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
    }

    public String getInstagam() {
        return instagam;
    }

    public void setInstagam(String instagam) {
        this.instagam = instagam;
    }

    public String getCurriculo() {
        return curriculo;
    }

    public void setCurriculo(String curriculo) {
        this.curriculo = curriculo;
    }
}