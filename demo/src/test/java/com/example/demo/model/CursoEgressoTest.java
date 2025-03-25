package com.example.demo.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CursoEgressoTest {

    @Test
    void testCursoEgressoGettersAndSetters() {
        // Instância da entidade CursoEgresso
        CursoEgresso cursoEgresso = new CursoEgresso();

        // Configurando valores
        Integer idCursoEgresso = 1;
        Integer anoInicio = 2015;
        Integer anoFim = 2019;

        Egresso egresso = new Egresso();
        egresso.setIdEgresso(1);
        egresso.setNome("João Silva");

        Curso curso = new Curso();
        curso.setIdCurso(1L);
        curso.setNome("Engenharia de Computação");
        curso.setNivel("Bacharelado");

        // Usando setters
        cursoEgresso.setIdCursoEgresso(idCursoEgresso);
        cursoEgresso.setAnoInicio(anoInicio);
        cursoEgresso.setAnoFim(anoFim);
        cursoEgresso.setEgresso(egresso);
        cursoEgresso.setCurso(curso);

        // Validando com getters
        assertEquals(idCursoEgresso, cursoEgresso.getIdCursoEgresso());
        assertEquals(anoInicio, cursoEgresso.getAnoInicio());
        assertEquals(anoFim, cursoEgresso.getAnoFim());
        assertEquals(egresso, cursoEgresso.getEgresso());
        assertEquals(curso, cursoEgresso.getCurso());
    }

    @Test
    void testCursoEgressoWithoutAnoFim() {
        // Instância da entidade CursoEgresso
        CursoEgresso cursoEgresso = new CursoEgresso();

        // Configurando valores
        Integer idCursoEgresso = 2;
        Integer anoInicio = 2020;

        Egresso egresso = new Egresso();
        egresso.setIdEgresso(2);
        egresso.setNome("Maria Oliveira");

        Curso curso = new Curso();
        curso.setIdCurso(2L);
        curso.setNome("Matemática");
        curso.setNivel("Licenciatura");

        // Usando setters
        cursoEgresso.setIdCursoEgresso(idCursoEgresso);
        cursoEgresso.setAnoInicio(anoInicio);
        cursoEgresso.setEgresso(egresso);
        cursoEgresso.setCurso(curso);

        // Validando com getters
        assertEquals(idCursoEgresso, cursoEgresso.getIdCursoEgresso());
        assertEquals(anoInicio, cursoEgresso.getAnoInicio());
        assertNull(cursoEgresso.getAnoFim()); // AnoFim não configurado
        assertEquals(egresso, cursoEgresso.getEgresso());
        assertEquals(curso, cursoEgresso.getCurso());
    }
}

