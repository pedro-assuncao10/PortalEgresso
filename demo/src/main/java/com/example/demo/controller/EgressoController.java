package com.example.demo.controller;

import com.example.demo.model.Egresso;
import com.example.demo.dto.EgressoCargoCursoDTO;
import com.example.demo.service.EgressoService;
import com.example.demo.dto.EgressoDTO;
import org.springframework.http.HttpStatus;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/egressos")
public class EgressoController {

    @Autowired
    private EgressoService egressoService;

    // Criar um novo egresso e associá-lo a um curso
    @PostMapping
    public ResponseEntity<Egresso> criarEgresso(@RequestBody EgressoDTO egressoDTO) {
        try {
            Egresso egressoCriado = egressoService.criarEgresso(egressoDTO);
            return ResponseEntity.ok(egressoCriado);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Listar todos os egressos
    @GetMapping
    public ResponseEntity<List<Egresso>> listarEgressos() {
        List<Egresso> egressos = egressoService.listarEgressos();
        return ResponseEntity.ok(egressos);
    }

    // Buscar egresso pelo ID
    @GetMapping("/{id}")
    public ResponseEntity<Egresso> buscarEgressoPorId(@PathVariable Integer id) {
        Egresso egresso = egressoService.buscarEgressoPorId(id);
        return ResponseEntity.ok(egresso);
    }

    // Atualizar um egresso existente
    @PutMapping("/{id}")
    public ResponseEntity<Egresso> editarEgresso(
            @PathVariable Integer id,
            @RequestBody Egresso egressoAtualizado,
            @RequestParam Long idCurso,
            @RequestParam Integer anoInicio,
            @RequestParam(required = false) Integer anoFim) {
        Egresso egressoEditado = egressoService.editarEgresso(id, egressoAtualizado, idCurso, anoInicio, anoFim);
        return ResponseEntity.ok(egressoEditado);
    }

    // Excluir um egresso pelo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirEgresso(@PathVariable Integer id) {
        egressoService.excluirEgresso(id);
        return ResponseEntity.noContent().build();
    }

    /*
    @GetMapping("/cargo-curso")
    public List<EgressoCargoCursoDTO> getEgressoCargoCurso() {
        return egressoService.getEgressoCargoCurso();
    }

    @GetMapping("/cargo-curso/{nome}")
    public List<EgressoCargoCursoDTO> getEgressoCargoCursoByName(@PathVariable String nome) {
        return egressoService.getEgressoCargoCursoByName(nome);
    }

 */
}