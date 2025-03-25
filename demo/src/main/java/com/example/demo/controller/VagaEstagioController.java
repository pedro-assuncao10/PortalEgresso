package com.example.demo.controller;

import com.example.demo.model.VagaEstagio;
import com.example.demo.service.VagaEstagioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/vagas")
public class VagaEstagioController {

    @Autowired
    private VagaEstagioService vagaEstagioService;

    // Criar nova vaga de estágio
    @PostMapping
    public ResponseEntity<VagaEstagio> criarVaga(@RequestBody VagaEstagio vagaEstagio) {
        VagaEstagio novaVaga = vagaEstagioService.criarVaga(vagaEstagio);
        return ResponseEntity.ok(novaVaga);
    }

    // Listar todas as vagas de estágio
    @GetMapping
    public ResponseEntity<List<VagaEstagio>> listarTodasVagas() {
        List<VagaEstagio> vagas = vagaEstagioService.listarTodasVagas();
        return ResponseEntity.ok(vagas);
    }

    // Buscar vaga de estágio por ID
    @GetMapping("/{id}")
    public ResponseEntity<VagaEstagio> buscarVagaPorId(@PathVariable Long id) {
        Optional<VagaEstagio> vaga = vagaEstagioService.buscarVagaPorId(id);
        return vaga.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }


    @GetMapping("/porEgresso/{idEgresso}")
    public ResponseEntity<List<VagaEstagio>> buscarVagasPorEgresso(@PathVariable Integer idEgresso) {
        List<VagaEstagio> vagas = vagaEstagioService.buscarVagasPorEgresso(idEgresso);
        return ResponseEntity.ok(vagas);
    }



    // Atualizar vaga de estágio existente
    @PutMapping("/{id}")
    public ResponseEntity<VagaEstagio> atualizarVaga(@PathVariable Long id, @RequestBody VagaEstagio vagaAtualizada) {
        try {
            VagaEstagio vaga = vagaEstagioService.atualizarVaga(id, vagaAtualizada);
            return ResponseEntity.ok(vaga);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Excluir vaga de estágio
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirVaga(@PathVariable Long id) {
        try {
            vagaEstagioService.excluirVaga(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Filtros avançados
    @GetMapping("/curso/{idCurso}")
    public ResponseEntity<List<VagaEstagio>> buscarVagasPorCurso(@PathVariable Long idCurso) {
        List<VagaEstagio> vagas = vagaEstagioService.buscarVagasPorCurso(idCurso);
        return ResponseEntity.ok(vagas);
    }

    @GetMapping("/tipo/{tipoEstagio}")
    public ResponseEntity<List<VagaEstagio>> buscarVagasPorTipoEstagio(@PathVariable String tipoEstagio) {
        List<VagaEstagio> vagas = vagaEstagioService.buscarVagasPorTipoEstagio(tipoEstagio);
        return ResponseEntity.ok(vagas);
    }

    @GetMapping("/localidade/{localidade}")
    public ResponseEntity<List<VagaEstagio>> buscarVagasPorLocalidade(@PathVariable String localidade) {
        List<VagaEstagio> vagas = vagaEstagioService.buscarVagasPorLocalidade(localidade);
        return ResponseEntity.ok(vagas);
    }

    @GetMapping("/recentes")
    public ResponseEntity<List<VagaEstagio>> buscarVagasRecentes() {
        List<VagaEstagio> vagas = vagaEstagioService.buscarVagasRecentes();
        return ResponseEntity.ok(vagas);
    }

    /*
    @GetMapping("/permissao-edicao/{idVaga}/{idEgresso}")
    public ResponseEntity<Boolean> verificarPermissaoEdicao(@PathVariable Long idVaga, @PathVariable Long idEgresso) {
        boolean permissao = vagaEstagioService.verificarPermissaoEdicao(idVaga, idEgresso);
        return ResponseEntity.ok(permissao);
    }*/
}
