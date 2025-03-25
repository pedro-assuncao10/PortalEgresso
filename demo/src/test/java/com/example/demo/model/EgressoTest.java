package com.example.demo.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EgressoTest {

    @Test
    void testEgressoGettersAndSetters() {
        // Instância da entidade Egresso
        Egresso egresso = new Egresso();

        // Configurando valores
        Integer idEgresso = 1;
        String nome = "João Silva";
        String email = "joao.silva@example.com";
        String descricao = "Um profissional dedicado.";
        String foto = "joao.jpg";
        String linkedin = "https://linkedin.com/in/joaosilva";
        String instagam = "@joaosilva";
        String curriculo = "Joao_CV.pdf";

        // Usando setters
        egresso.setIdEgresso(idEgresso);
        egresso.setNome(nome);
        egresso.setEmail(email);
        egresso.setDescricao(descricao);
        egresso.setFoto(foto);
        egresso.setLinkedin(linkedin);
        egresso.setInstagam(instagam);
        egresso.setCurriculo(curriculo);

        // Validando com getters
        assertEquals(idEgresso, egresso.getIdEgresso());
        assertEquals(nome, egresso.getNome());
        assertEquals(email, egresso.getEmail());
        assertEquals(descricao, egresso.getDescricao());
        assertEquals(foto, egresso.getFoto());
        assertEquals(linkedin, egresso.getLinkedin());
        assertEquals(instagam, egresso.getInstagam());
        assertEquals(curriculo, egresso.getCurriculo());
    }

    @Test
    void testEgressoWithMinimalAttributes() {
        // Instância da entidade Egresso
        Egresso egresso = new Egresso();

        // Configurando valores mínimos
        Integer idEgresso = 2;
        String nome = "Maria Oliveira";
        String email = "maria.oliveira@example.com";

        // Usando setters
        egresso.setIdEgresso(idEgresso);
        egresso.setNome(nome);
        egresso.setEmail(email);

        // Validando com getters
        assertEquals(idEgresso, egresso.getIdEgresso());
        assertEquals(nome, egresso.getNome());
        assertEquals(email, egresso.getEmail());

        // Atributos opcionais devem ser nulos
        assertNull(egresso.getDescricao());
        assertNull(egresso.getFoto());
        assertNull(egresso.getLinkedin());
        assertNull(egresso.getInstagam());
        assertNull(egresso.getCurriculo());
    }
}

