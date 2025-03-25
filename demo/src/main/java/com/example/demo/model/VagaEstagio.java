package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "vaga_estagio")
public class VagaEstagio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vaga", nullable = false)
    private Long idVaga;

    @Column(name = "titulo", nullable = false)
    private String titulo;

    @Column(name = "descricao", nullable = false, columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "tipo_estagio", nullable = false)
    private String tipoEstagio;

    @Column(name = "localidade")
    private String localidade;

    @Column(name = "data_publicacao", nullable = false)
    private LocalDate dataPublicacao = LocalDate.now();

    @ManyToOne
    @JoinColumn(name = "id_egresso", nullable = false)
    private Egresso egresso;

    @ManyToMany
    @JoinTable(
            name = "vaga_curso",
            joinColumns = @JoinColumn(name = "id_vaga"),
            inverseJoinColumns = @JoinColumn(name = "id_curso")
    )
    private List<Curso> cursosDestinados;

    // Getters e Setters
    public Long getIdVaga() {
        return idVaga;
    }

    public void setIdVaga(Long idVaga) {
        this.idVaga = idVaga;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipoEstagio() {
        return tipoEstagio;
    }

    public void setTipoEstagio(String tipoEstagio) {
        this.tipoEstagio = tipoEstagio;
    }

    public String getLocalidade() {
        return localidade;
    }

    public void setLocalidade(String localidade) {
        this.localidade = localidade;
    }

    public LocalDate getDataPublicacao() {
        return dataPublicacao;
    }

    public void setDataPublicacao(LocalDate dataPublicacao) {
        this.dataPublicacao = dataPublicacao;
    }

    public Egresso getEgresso() {
        return egresso;
    }

    public void setEgresso(Egresso egresso) {
        this.egresso = egresso;
    }

    public List<Curso> getCursosDestinados() {
        return cursosDestinados;
    }

    public void setCursosDestinados(List<Curso> cursosDestinados) {
        this.cursosDestinados = cursosDestinados;
    }
}
