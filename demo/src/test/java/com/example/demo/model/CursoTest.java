package com.example.demo.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CursoTest {

    @Test
    void testCursoGettersAndSetters() {
        // Instância da entidade Curso
        Curso curso = new Curso();

        // Configurando valores
        Long idCurso = 1L;
        String nome = "Engenharia de Software";
        String nivel = "Bacharelado";
        Coordenador coordenador = new Coordenador();
        coordenador.setIdCoordenador(1L);
        coordenador.setLogin("coordenador.ufma");
        coordenador.setSenha("123456");
        coordenador.setTipo("administrador");

        // Usando setters
        curso.setIdCurso(idCurso);
        curso.setNome(nome);
        curso.setNivel(nivel);
        curso.setCoordenador(coordenador);

        // Validando com getters
        assertEquals(idCurso, curso.getIdCurso());
        assertEquals(nome, curso.getNome());
        assertEquals(nivel, curso.getNivel());
        assertEquals(coordenador, curso.getCoordenador());
    }

    @Test
    void testCursoWithoutCoordenador() {
        // Instância da entidade Curso
        Curso curso = new Curso();

        // Configurando valores
        Long idCurso = 2L;
        String nome = "Matemática";
        String nivel = "Licenciatura";

        // Usando setters
        curso.setIdCurso(idCurso);
        curso.setNome(nome);
        curso.setNivel(nivel);

        // Validando com getters
        assertEquals(idCurso, curso.getIdCurso());
        assertEquals(nome, curso.getNome());
        assertEquals(nivel, curso.getNivel());
        assertNull(curso.getCoordenador()); // Coordenador não foi configurado
    }
}

