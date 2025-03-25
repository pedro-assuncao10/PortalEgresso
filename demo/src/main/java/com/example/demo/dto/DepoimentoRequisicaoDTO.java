package com.example.demo.dto;

import com.example.demo.model.Depoimento;
import java.util.Date;

public class DepoimentoRequisicaoDTO {

    private Integer idDepoimento;
    private Integer idEgresso;
    private String texto;
    private Date data;

    // Getters e Setters
    public Integer getIdDepoimento() {
        return idDepoimento;
    }

    public void setIdDepoimento(Integer idDepoimento) {
        this.idDepoimento = idDepoimento;
    }

    public Integer getIdEgresso() {
        return idEgresso;
    }

    public void setIdEgresso(Integer idEgresso) {
        this.idEgresso = idEgresso;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    // Método para converter DTO em entidade Depoimento
    public Depoimento toDepoimento() {
        Depoimento depoimento = new Depoimento();
        depoimento.setIdDepoimento(this.idDepoimento);
        depoimento.setTexto(this.texto);  // Certifique-se de que o texto está sendo mapeado
        depoimento.setData(this.data);  // Se a data for opcional, verifique se ela não é nula
        return depoimento;
    }
}
