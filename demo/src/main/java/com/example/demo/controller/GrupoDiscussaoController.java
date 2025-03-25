package com.example.demo.controller;

import com.example.demo.service.GrupoDiscussaoService;
import com.example.demo.model.GrupoDiscussao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;
import jakarta.validation.Valid;
import java.util.Collections;
import java.util.Optional;



@RestController
@RequestMapping("/api/grupos")
public class GrupoDiscussaoController {

    @Autowired
    private GrupoDiscussaoService grupoDiscussaoService;

    @PostMapping
    public ResponseEntity<GrupoDiscussao> criarGrupo(@RequestBody GrupoDiscussao grupo) {
        GrupoDiscussao novoGrupo = grupoDiscussaoService.criarGrupo(grupo);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoGrupo);
    }

    @GetMapping
    public ResponseEntity<List<GrupoDiscussao>> listarGrupos() {
        List<GrupoDiscussao> grupos = grupoDiscussaoService.listarGrupos();

        // Verifique se o retorno da API é uma lista
        if (grupos == null || grupos.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList()); // Se não houver grupos, retorna uma lista vazia
        }

        return ResponseEntity.ok(grupos);
    }


    @PostMapping("/{grupoId}/participantes/{egressoId}")
    public ResponseEntity<GrupoDiscussao> adicionarParticipante(
            @PathVariable Long grupoId, @PathVariable Long egressoId) {
        GrupoDiscussao grupoAtualizado = grupoDiscussaoService.adicionarParticipante(grupoId, egressoId);
        return ResponseEntity.ok(grupoAtualizado);
    }

    @GetMapping("/{grupoId}")
    public ResponseEntity<GrupoDiscussao> buscarGrupoPorId(@PathVariable Long grupoId) {
        Optional<GrupoDiscussao> grupo = grupoDiscussaoService.buscarGrupo(grupoId);

        if (!grupo.isPresent()) {
            return ResponseEntity.notFound().build(); // Retorna 404 caso o grupo não seja encontrado
        }

        return ResponseEntity.ok(grupo.get());
    }


}

