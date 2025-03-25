package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.persistence.Entity;

@Entity
@Table(name = "curso_egresso")
public class CursoEgresso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_curso_egresso", nullable = false)
    private Integer idCursoEgresso;

    @ManyToOne
    @JoinColumn(name = "id_egresso", nullable = false)
    private Egresso egresso;

    @ManyToOne
    @JoinColumn(name = "id_curso", nullable = false)
    private Curso curso;

    @Column(name = "ano_inicio", nullable = false)
    private Integer anoInicio;

    @Column(name = "ano_fim")
    private Integer anoFim;


    //getters e setters
    public Integer getIdCursoEgresso() {
        return idCursoEgresso;
    }

    public void setIdCursoEgresso(Integer idCursoEgresso) {
        this.idCursoEgresso = idCursoEgresso;
    }

    public Integer getAnoFim() {
        return anoFim;
    }

    public void setAnoFim(Integer anoFim) {
        this.anoFim = anoFim;
    }

    public Integer getAnoInicio() {
        return anoInicio;
    }

    public void setAnoInicio(Integer anoInicio) {
        this.anoInicio = anoInicio;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public Egresso getEgresso() {
        return egresso;
    }

    public void setEgresso(Egresso egresso) {
        this.egresso = egresso;
    }
}
