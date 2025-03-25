package com.example.demo.service;

import com.example.demo.model.Depoimento;
import com.example.demo.model.Egresso;
import com.example.demo.repository.EgressoRepository;
import com.example.demo.repository.DepoimentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Date;

@Service
public class DepoimentoService {

    @Autowired
    private DepoimentoRepository depoimentoRepository;

    @Autowired
    private EgressoRepository egressoRepository;

    /**
     * Cria um novo depoimento associado a um egresso.
     *
     * @param idEgresso  ID do egresso ao qual o depoimento será associado.
     * @param depoimento Depoimento a ser criado.
     * @return Depoimento salvo.
     */
    @Transactional
    public Depoimento criarDepoimento(Integer idEgresso, Depoimento depoimento) {
        // Verifica se o egresso existe e o associa ao depoimento
        Egresso egresso = egressoRepository.findById(idEgresso)
                .orElseThrow(() -> new IllegalArgumentException("Egresso com ID " + idEgresso + " não encontrado"));

        depoimento.setEgresso(egresso);
        return depoimentoRepository.save(depoimento);
    }

    /**
     * Lista todos os depoimentos de um egresso.
     *
     * @param idEgresso ID do egresso.
     * @return Lista de depoimentos do egresso.
     */
    public List<Depoimento> listarDepoimentosPorEgresso(Integer idEgresso) {
        if (!egressoRepository.existsById(idEgresso)) {
            throw new IllegalArgumentException("Egresso com ID " + idEgresso + " não encontrado");
        }
        // Chamando o método correto do repositório
        return depoimentoRepository.findByEgresso_IdEgresso(idEgresso);
    }

    /**
     * Atualiza um depoimento existente.
     *
     * @param idDepoimento  ID do depoimento a ser atualizado.
     * @param novoDepoimento Dados atualizados do depoimento.
     * @return Depoimento atualizado.
     */
    @Transactional
    public Depoimento atualizarDepoimento(Integer idDepoimento, Depoimento novoDepoimento) {
        Depoimento depoimentoExistente = depoimentoRepository.findById(idDepoimento)
                .orElseThrow(() -> new IllegalArgumentException("Depoimento com ID " + idDepoimento + " não encontrado"));

        depoimentoExistente.setTexto(novoDepoimento.getTexto());
        depoimentoExistente.setData(novoDepoimento.getData());
        return depoimentoRepository.save(depoimentoExistente);
    }

    /**
     * Deleta um depoimento.
     *
     * @param idDepoimento ID do depoimento a ser deletado.
     */
    @Transactional
    public void deletarDepoimento(Integer idDepoimento) {
        if (!depoimentoRepository.existsById(idDepoimento)) {
            throw new IllegalArgumentException("Depoimento com ID " + idDepoimento + " não encontrado");
        }
        depoimentoRepository.deleteById(idDepoimento);
    }

    /**
     * Lista todos os depoimentos de todos os egressos.
     *
     * @return Lista de todos os depoimentos.
     */
    public List<Depoimento> listarTodosDepoimentos() {
        return depoimentoRepository.findAll();
    }

    /**
     * Busca um depoimento pelo ID.
     *
     * @param id ID do depoimento.
     * @return Depoimento correspondente ao ID.
     */
    public Depoimento buscarDepoimentoPorId(Integer id) {
        return depoimentoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Depoimento com ID " + id + " não encontrado"));
    }

    /**
     * Busca depoimentos que contenham um texto específico.
     *
     * @param texto Texto a ser buscado.
     * @return Lista de depoimentos que contenham o texto.
     */
    public List<Depoimento> buscarDepoimentosPorTexto(String texto) {
        return depoimentoRepository.findByTextoContaining(texto);
    }

    /**
     * Busca depoimentos dentro de um intervalo de datas.
     *
     * @param startDate Data inicial do intervalo.
     * @param endDate   Data final do intervalo.
     * @return Lista de depoimentos dentro do intervalo.
     */
    public List<Depoimento> buscarDepoimentosPorIntervaloDeDatas(Date startDate, Date endDate) {
        return depoimentoRepository.findByDateRange(startDate, endDate);
    }

    /**
     * Busca os depoimentos mais recentes.
     *
     * @return Lista de depoimentos ordenados por data (mais recentes primeiro).
     */
    public List<Depoimento> buscarDepoimentosMaisRecentes() {
        return depoimentoRepository.findLatestDepoimentos();
    }
}
