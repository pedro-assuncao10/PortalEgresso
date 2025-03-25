package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.persistence.Entity;

import java.util.List;

@Entity
@Table(name = "curso")
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_curso", nullable = false)
    private Long idCurso;

    @ManyToOne
    @JoinColumn(name = "id_coodenador")
    private Coordenador coordenador;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "nivel", nullable = false)
    private String nivel;

    // Relacionamento ManyToMany com VagaEstagio
    @ManyToMany(mappedBy = "cursosDestinados")  // Refere-se ao campo "cursosDestinados" em VagaEstagio
    private List<VagaEstagio> vagasEstagio;

    //getters and setters
    public Long getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(Long idCurso) {
        this.idCurso = idCurso;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Coordenador getCoordenador() {
        return coordenador;
    }

    public void setCoordenador(Coordenador coordenador) {
        this.coordenador = coordenador;
    }


}
