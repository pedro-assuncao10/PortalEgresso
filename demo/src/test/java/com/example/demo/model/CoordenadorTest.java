package com.example.demo.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CoordenadorTest {

    @Test
    void testCoordenadorGettersAndSetters() {
        // Instância da entidade Coordenador
        Coordenador coordenador = new Coordenador();

        // Configurando valores
        Long idCoordenador = 1L;
        String login = "coordenador.ufma";
        String senha = "123456";
        String tipo = "administrador";

        // Usando setters
        coordenador.setIdCoordenador(idCoordenador);
        coordenador.setLogin(login);
        coordenador.setSenha(senha);
        coordenador.setTipo(tipo);

        // Validando com getters
        assertEquals(idCoordenador, coordenador.getIdCoordenador());
        assertEquals(login, coordenador.getLogin());
        assertEquals(senha, coordenador.getSenha());
        assertEquals(tipo, coordenador.getTipo());
    }

    @Test
    void testCoordenadorDefaultTipo() {
        // Instância da entidade Coordenador
        Coordenador coordenador = new Coordenador();

        // Validando que o tipo padrão é "egresso"
        assertEquals("egresso", coordenador.getTipo());
    }
}
