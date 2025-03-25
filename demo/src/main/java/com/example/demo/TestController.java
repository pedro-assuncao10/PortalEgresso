package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/test-db")
    public String testeDb() {
        try {
            jdbcTemplate.execute("SELECT 1");
            return "Conexão com o banco está funcionando!";
        } catch (Exception e) {
            return "Erro na conexão com o banco: " + e.getMessage();
        }
    }
}
