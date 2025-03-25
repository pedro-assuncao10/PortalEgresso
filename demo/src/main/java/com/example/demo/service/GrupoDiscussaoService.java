package com.example.demo.service;

import com.example.demo.model.GrupoDiscussao;
import com.example.demo.model.Egresso;
import com.example.demo.repository.GrupoDiscussaoRepository;
import com.example.demo.repository.EgressoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

@Service
public class GrupoDiscussaoService {

    @Autowired
    private GrupoDiscussaoRepository grupoDiscussaoRepository;

    @Autowired
    private EgressoRepository egressoRepository;

    public GrupoDiscussao criarGrupo(GrupoDiscussao grupo) {
        // Validações para o criador e participantes
        if (grupo.getCriador() == null || !egressoRepository.existsById(grupo.getCriador().getIdEgresso())) {
            throw new RuntimeException("Criador não encontrado");
        }

        for (Egresso participante : grupo.getParticipantes()) {
            if (!egressoRepository.existsById(participante.getIdEgresso())) {
                throw new RuntimeException("Participante com ID " + participante.getIdEgresso() + " não encontrado");
            }
        }

        return grupoDiscussaoRepository.save(grupo);
    }

    public List<GrupoDiscussao> listarGrupos() {
        return grupoDiscussaoRepository.findAll();
    }

    public Optional<GrupoDiscussao> buscarGrupo(Long id) {
        return grupoDiscussaoRepository.findById(id);
    }

    public GrupoDiscussao adicionarParticipante(Long grupoId, Long egressoId) {
        GrupoDiscussao grupo = grupoDiscussaoRepository.findById(grupoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Grupo não encontrado"));

        Integer egressoIdInt = egressoId.intValue();  // Conversão explícita de Long para Integer

        Egresso egresso = egressoRepository.findById(egressoIdInt)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Egresso não encontrado"));

        if (!grupo.getParticipantes().contains(egresso)) {
            grupo.getParticipantes().add(egresso);
            grupoDiscussaoRepository.save(grupo);
        }

        return grupo;
    }

}

