package com.example.demo.controller;

import com.example.demo.dto.CargoRequisicaoDTO;
import com.example.demo.model.Cargo;
import com.example.demo.service.CargoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cargos")
public class CargoController {

    private final CargoService cargoService;

    @Autowired
    public CargoController(CargoService cargoService) {
        this.cargoService = cargoService;
    }

    @PostMapping("/criar")
    public ResponseEntity<Cargo> criarCargo(@RequestBody CargoRequisicaoDTO cargoDTO) {
        Cargo novoCargo = new Cargo();
        novoCargo.setDescricao(cargoDTO.getDescricao());
        novoCargo.setLocal(cargoDTO.getLocal());
        novoCargo.setAnoInicio(cargoDTO.getAnoInicio());
        novoCargo.setAnoFim(cargoDTO.getAnoFim());

        Cargo cargoCriado = cargoService.criarCargo(cargoDTO.getIdEgresso(), novoCargo);
        return ResponseEntity.ok(cargoCriado);
    }

    @GetMapping("/todos")
    public ResponseEntity<List<Cargo>> buscarTodosOsCargos() {
        return ResponseEntity.ok(cargoService.buscarTodosOsCargos());
    }

    @GetMapping("/porEgresso/{idEgresso}")
    public ResponseEntity<List<Cargo>> buscarCargosPorEgresso(@PathVariable Integer idEgresso) {
        List<Cargo> cargos = cargoService.buscarCargosPorEgresso(idEgresso);
        return ResponseEntity.ok(cargos);
    }

    @PutMapping("/atualizar")
    public ResponseEntity<Cargo> atualizarCargo(@RequestBody CargoRequisicaoDTO cargoDTO) {
        Cargo novoCargo = new Cargo();
        novoCargo.setDescricao(cargoDTO.getDescricao());
        novoCargo.setLocal(cargoDTO.getLocal());
        novoCargo.setAnoInicio(cargoDTO.getAnoInicio());
        novoCargo.setAnoFim(cargoDTO.getAnoFim());

        Cargo cargoAtualizado = cargoService.atualizarCargo(cargoDTO.getIdCargo(), novoCargo);
        return ResponseEntity.ok(cargoAtualizado);
    }

    @DeleteMapping("/deletar")
    public ResponseEntity<String> deletarCargo(@RequestBody CargoRequisicaoDTO cargoDTO) {
        cargoService.deletarCargo(cargoDTO.getIdCargo());
        return ResponseEntity.ok("Cargo deletado com sucesso!");
    }
}
