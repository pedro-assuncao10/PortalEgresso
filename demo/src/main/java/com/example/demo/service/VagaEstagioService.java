package com.example.demo.service;

import com.example.demo.model.VagaEstagio;
import com.example.demo.repository.VagaEstagioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VagaEstagioService {

    @Autowired
    private VagaEstagioRepository vagaEstagioRepository;

    // Criar nova vaga de estágio
    public VagaEstagio criarVaga(VagaEstagio vagaEstagio) {
        return vagaEstagioRepository.save(vagaEstagio);
    }

    // Listar todas as vagas de estágio
    public List<VagaEstagio> listarTodasVagas() {
        return vagaEstagioRepository.findAll();
    }

    // Buscar vaga de estágio por ID
    public Optional<VagaEstagio> buscarVagaPorId(Long id) {
        return vagaEstagioRepository.findById(id);
    }

    // Atualizar vaga de estágio existente
    public VagaEstagio atualizarVaga(Long id, VagaEstagio vagaAtualizada) {
        return vagaEstagioRepository.findById(id)
                .map(vagaExistente -> {
                    vagaExistente.setTitulo(vagaAtualizada.getTitulo());
                    vagaExistente.setDescricao(vagaAtualizada.getDescricao());
                    vagaExistente.setTipoEstagio(vagaAtualizada.getTipoEstagio());
                    vagaExistente.setLocalidade(vagaAtualizada.getLocalidade());
                    vagaExistente.setCursosDestinados(vagaAtualizada.getCursosDestinados());
                    return vagaEstagioRepository.save(vagaExistente);
                })
                .orElseThrow(() -> new RuntimeException("Vaga de estágio não encontrada com o ID: " + id));
    }

    // Excluir vaga de estágio por ID
    public void excluirVaga(Long id) {
        if (vagaEstagioRepository.existsById(id)) {
            vagaEstagioRepository.deleteById(id);
        } else {
            throw new RuntimeException("Vaga de estágio não encontrada com o ID: " + id);
        }
    }

    // Filtros de busca usando os métodos padrões do Spring Data JPA
    public List<VagaEstagio> buscarVagasPorCurso(Long idCurso) {
        return vagaEstagioRepository.findByCursoId(idCurso);
    }

    public List<VagaEstagio> buscarVagasPorTipoEstagio(String tipoEstagio) {
        return vagaEstagioRepository.findByTipoEstagio(tipoEstagio);
    }

    public List<VagaEstagio> buscarVagasPorLocalidade(String localidade) {
        return vagaEstagioRepository.findByLocalidade(localidade);
    }

    public List<VagaEstagio> buscarVagasRecentes() {
        return vagaEstagioRepository.findAllByOrderByDataPublicacaoDesc();
    }

    // Buscar vagas por Egresso
    public List<VagaEstagio> buscarVagasPorEgresso(Integer idEgresso) {
        return vagaEstagioRepository.findByEgresso_IdEgresso(idEgresso);
    }


    /*public boolean verificarPermissaoEdicao(Long idVaga, Long idEgresso) {
        return vagaEstagioRepository.existsByIdVagaAndEgressoIdEgresso(idVaga, idEgresso);
    }*/
}
