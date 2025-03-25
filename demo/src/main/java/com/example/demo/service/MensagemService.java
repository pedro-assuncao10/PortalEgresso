package com.example.demo.service;

import com.example.demo.model.Mensagem;
import com.example.demo.model.GrupoDiscussao;
import com.example.demo.repository.MensagemRepository;
import com.example.demo.repository.GrupoDiscussaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.time.LocalDateTime;

@Service
public class MensagemService {

    @Autowired
    private MensagemRepository mensagemRepository;

    @Autowired
    private GrupoDiscussaoRepository grupoDiscussaoRepository;

    public Mensagem enviarMensagem(Long grupoId, Mensagem mensagem) {
        GrupoDiscussao grupo = grupoDiscussaoRepository.findById(grupoId)
                .orElseThrow(() -> new RuntimeException("Grupo não encontrado"));

        mensagem.setDataEnvio(LocalDateTime.now());
        mensagem.setGrupo(grupo);

        return mensagemRepository.save(mensagem);
    }

    public List<Mensagem> listarMensagens(Long grupoId) {
        return mensagemRepository.findByGrupoIdOrderByDataEnvioAsc(grupoId);
    }
}
