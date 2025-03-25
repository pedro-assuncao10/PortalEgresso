package com.example.demo.model;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class DepoimentoTest {

    @Test
    void testDepoimentoGettersAndSetters() {
        // Instância da entidade Depoimento
        Depoimento depoimento = new Depoimento();

        // Configurando valores
        Integer idDepoimento = 1;
        String texto = "Este é um depoimento de teste.";
        Date data = new Date();

        Egresso egresso = new Egresso();
        egresso.setIdEgresso(1);
        egresso.setNome("João Silva");

        // Usando setters
        depoimento.setIdDepoimento(idDepoimento);
        depoimento.setTexto(texto);
        depoimento.setData(data);
        depoimento.setEgresso(egresso);

        // Validando com getters
        assertEquals(idDepoimento, depoimento.getIdDepoimento());
        assertEquals(texto, depoimento.getTexto());
        assertEquals(data, depoimento.getData());
        assertEquals(egresso, depoimento.getEgresso());
    }

    @Test
    void testDepoimentoWithoutTextoAndData() {
        // Instância da entidade Depoimento
        Depoimento depoimento = new Depoimento();

        // Configurando valores
        Integer idDepoimento = 2;

        Egresso egresso = new Egresso();
        egresso.setIdEgresso(2);
        egresso.setNome("Maria Oliveira");

        // Usando setters
        depoimento.setIdDepoimento(idDepoimento);
        depoimento.setEgresso(egresso);

        // Validando com getters
        assertEquals(idDepoimento, depoimento.getIdDepoimento());
        assertNull(depoimento.getTexto()); // Texto não configurado
        assertNull(depoimento.getData()); // Data não configurada
        assertEquals(egresso, depoimento.getEgresso());
    }
}

