package com.example.demo.controller;

import com.example.demo.dto.DepoimentoRequisicaoDTO;
import com.example.demo.model.Depoimento;
import com.example.demo.service.DepoimentoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/depoimentos")
public class DepoimentoController {

    private final DepoimentoService depoimentoService;

    public DepoimentoController(DepoimentoService depoimentoService) {
        this.depoimentoService = depoimentoService;
    }

    /**
     * Endpoint para buscar todos os depoimentos.
     */
    @GetMapping
    public ResponseEntity<List<Depoimento>> listarTodosDepoimentos() {
        List<Depoimento> depoimentos = depoimentoService.listarTodosDepoimentos();
        return ResponseEntity.ok(depoimentos);
    }

    /**
     * Endpoint para criar um novo depoimento.
     *
     * @param dto Objeto contendo os dados necessários para criar um depoimento.
     * @return Depoimento criado.
     */
    @PostMapping
    public ResponseEntity<Depoimento> criarDepoimento(@RequestBody DepoimentoRequisicaoDTO dto) {
        Depoimento novoDepoimento = depoimentoService.criarDepoimento(dto.getIdEgresso(), dto.toDepoimento());
        return ResponseEntity.status(HttpStatus.CREATED).body(novoDepoimento);
    }

    /**
     * Endpoint para deletar um depoimento por ID.
     *
     * @param dto Objeto contendo o ID do depoimento a ser deletado.
     * @return Status de sucesso.
     */
    @DeleteMapping
    public ResponseEntity<Void> deletarDepoimento(@RequestBody DepoimentoRequisicaoDTO dto) {
        depoimentoService.deletarDepoimento(dto.getIdDepoimento());
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint para buscar um depoimento por ID.
     *
     * @param dto Objeto contendo o ID do depoimento a ser buscado.
     * @return Depoimento encontrado.
     */
    @PostMapping("/buscar")
    public ResponseEntity<Depoimento> buscarDepoimentoPorId(@RequestBody DepoimentoRequisicaoDTO dto) {
        Depoimento depoimento = depoimentoService.buscarDepoimentoPorId(dto.getIdDepoimento());
        return ResponseEntity.ok(depoimento);
    }

    /**
     * Endpoint para atualizar um depoimento.
     *
     * @param dto Objeto contendo os dados necessários para atualizar um depoimento.
     * @return Depoimento atualizado.
     */
    @PutMapping
    public ResponseEntity<Depoimento> atualizarDepoimento(@RequestBody DepoimentoRequisicaoDTO dto) {
        Depoimento depoimentoAtualizado = depoimentoService.atualizarDepoimento(dto.getIdDepoimento(), dto.toDepoimento());
        return ResponseEntity.ok(depoimentoAtualizado);
    }

    @GetMapping("/porEgresso/{idEgresso}")
    public ResponseEntity<List<Depoimento>> listarDepoimentosPorEgresso(@PathVariable Integer idEgresso) {
        List<Depoimento> depoimentos = depoimentoService.listarDepoimentosPorEgresso(idEgresso);
        return ResponseEntity.ok(depoimentos);
    }
}
