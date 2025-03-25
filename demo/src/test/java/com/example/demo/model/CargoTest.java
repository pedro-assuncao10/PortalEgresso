package com.example.demo.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CargoTest {

    @Test
    void testCargoGettersAndSetters() {
        // Instância da entidade Cargo
        Cargo cargo = new Cargo();

        // Configurando valores
        Integer idCargo = 1;
        Integer anoInicio = 2020;
        Integer anoFim = 2025;
        String descricao = "Engenheiro de Software";
        String local = "São Paulo";
        Egresso egresso = new Egresso();
        egresso.setIdEgresso(10);

        // Usando setters
        cargo.setIdCargo(idCargo);
        cargo.setAnoInicio(anoInicio);
        cargo.setAnoFim(anoFim);
        cargo.setDescricao(descricao);
        cargo.setLocal(local);
        cargo.setEgresso(egresso);

        // Validando com getters
        assertEquals(idCargo, cargo.getIdCargo());
        assertEquals(anoInicio, cargo.getAnoInicio());
        assertEquals(anoFim, cargo.getAnoFim());
        assertEquals(descricao, cargo.getDescricao());
        assertEquals(local, cargo.getLocal());
        assertEquals(egresso, cargo.getEgresso());
    }

    @Test
    void testCargoAnoFimCanBeNull() {
        // Instância da entidade Cargo
        Cargo cargo = new Cargo();

        // Configurando valores
        Integer idCargo = 2;
        Integer anoInicio = 2019;
        String descricao = "Desenvolvedor Web";
        String local = "Rio de Janeiro";

        // Usando setters
        cargo.setIdCargo(idCargo);
        cargo.setAnoInicio(anoInicio);
        cargo.setAnoFim(null);
        cargo.setDescricao(descricao);
        cargo.setLocal(local);

        // Validando com getters
        assertEquals(idCargo, cargo.getIdCargo());
        assertEquals(anoInicio, cargo.getAnoInicio());
        assertNull(cargo.getAnoFim()); // AnoFim pode ser nulo
        assertEquals(descricao, cargo.getDescricao());
        assertEquals(local, cargo.getLocal());
    }
}
