package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import java.util.Date;

@Entity
@Table(name = "depoimento")
public class Depoimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_depoimento", nullable = false)
    private Integer idDepoimento;

    @ManyToOne
    @JoinColumn(name = "id_egresso", nullable = false)
    private Egresso egresso;

    @Column(name = "texto")
    private String texto;

    @Column(name = "data")
    @Temporal(TemporalType.DATE)
    private Date data;

    // Getters and Setters

    public Integer getIdDepoimento() {
        return idDepoimento;
    }

    public void setIdDepoimento(Integer idDepoimento) {
        this.idDepoimento = idDepoimento;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Egresso getEgresso() {
        return egresso;
    }

    public void setEgresso(Egresso egresso) {
        this.egresso = egresso;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
}
