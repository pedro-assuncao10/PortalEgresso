package com.example.demo.controller;

import com.example.demo.service.CursoEgressoService;
import com.example.demo.model.CursoEgresso;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/curso-egresso")
public class CursoEgressoController {
    @Autowired
    private CursoEgressoService service;

    @GetMapping("/todos")
    public ResponseEntity<List<CursoEgresso>> listarTodos() {
        List<CursoEgresso> lista = service.listarTodos();
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }
}
