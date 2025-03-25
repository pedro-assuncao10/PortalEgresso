package com.example.demo.service;

import com.example.demo.model.Cargo;
import com.example.demo.model.Egresso;
import com.example.demo.repository.CargoRepository;
import com.example.demo.repository.EgressoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CargoService {

    private final CargoRepository cargoRepository;
    private final EgressoRepository egressoRepository;

    @Autowired
    public CargoService(CargoRepository cargoRepository, EgressoRepository egressoRepository) {
        this.cargoRepository = cargoRepository;
        this.egressoRepository = egressoRepository;
    }

    // Criar um novo cargo associado a um egresso
    public Cargo criarCargo(Integer idEgresso, Cargo cargo) {
        Egresso egresso = egressoRepository.findById(idEgresso)
                .orElseThrow(() -> new RuntimeException("Egresso com ID " + idEgresso + " não encontrado"));
        cargo.setEgresso(egresso);
        return cargoRepository.save(cargo);
    }

    // Buscar todos os cargos de um egresso
    public List<Cargo> buscarCargosPorEgresso(Integer idEgresso) {
        return cargoRepository.findByEgresso_IdEgresso(idEgresso);
    }

    // Buscar todos os cargos de todos os egressos
    public List<Cargo> buscarTodosOsCargos() {
        return cargoRepository.findAll();
    }

    // Atualizar um cargo existente
    public Cargo atualizarCargo(Integer idCargo, Cargo novoCargo) {
        Cargo cargoExistente = cargoRepository.findById(idCargo)
                .orElseThrow(() -> new RuntimeException("Cargo com ID " + idCargo + " não encontrado"));

        // Atualizando os campos de Cargo
        cargoExistente.setDescricao(novoCargo.getDescricao());
        cargoExistente.setLocal(novoCargo.getLocal());
        cargoExistente.setAnoInicio(novoCargo.getAnoInicio());
        cargoExistente.setAnoFim(novoCargo.getAnoFim());

        return cargoRepository.save(cargoExistente);
    }

    // Deletar um cargo pelo ID
    public void deletarCargo(Integer idCargo) {
        if (!cargoRepository.existsById(idCargo)) {
            throw new RuntimeException("Cargo com ID " + idCargo + " não encontrado");
        }
        cargoRepository.deleteById(idCargo);
    }

    // Buscar cargos por local de trabalho
    public List<Cargo> buscarCargosPorLocal(String local) {
        return cargoRepository.findByLocalContaining(local);
    }

    // Buscar cargos por intervalo de anos
    public List<Cargo> buscarCargosPorAnoRange(Integer anoInicio, Integer anoFim) {
        return cargoRepository.findByAnoRange(anoInicio, anoFim);
    }

    // Buscar cargos por descrição
    public List<Cargo> buscarCargosPorDescricao(String descricao) {
        return cargoRepository.findByDescricaoContaining(descricao);
    }
}
