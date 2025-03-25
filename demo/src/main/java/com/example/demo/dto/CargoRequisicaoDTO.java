package com.example.demo.dto;

public class CargoRequisicaoDTO {

    private Integer idCargo;
    private Integer idEgresso;
    private String descricao;
    private String local;
    private Integer anoInicio;
    private Integer anoFim;

    // Getters e Setters
    public Integer getIdCargo() {
        return idCargo;
    }

    public void setIdCargo(Integer idCargo) {
        this.idCargo = idCargo;
    }

    public Integer getIdEgresso() {
        return idEgresso;
    }

    public void setIdEgresso(Integer idEgresso) {
        this.idEgresso = idEgresso;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public Integer getAnoInicio() {
        return anoInicio;
    }

    public void setAnoInicio(Integer anoInicio) {
        this.anoInicio = anoInicio;
    }

    public Integer getAnoFim() {
        return anoFim;
    }

    public void setAnoFim(Integer anoFim) {
        this.anoFim = anoFim;
    }
}
