package com.example.demo.service;

import com.example.demo.model.Egresso;
import com.example.demo.model.CursoEgresso;
import com.example.demo.model.Curso;
import com.example.demo.dto.EgressoDTO;
import com.example.demo.dto.EgressoCargoCursoDTO;

import com.example.demo.repository.CursoRepository;
import com.example.demo.repository.CursoEgressoRepository;
import com.example.demo.repository.EgressoRepository;
import com.example.demo.repository.CargoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;

@Service
public class EgressoService {

    @Autowired
    private EgressoRepository egressoRepository;

    @Autowired
    private CursoRepository cursoRepository; // Nome consistente em todo o código

    @Autowired
    private CargoRepository cargoRepository;

    @Autowired
    private CursoEgressoRepository cursoEgressoRepository;

    //criar egresso
    public Egresso criarEgresso(EgressoDTO egressoDTO) {
        try {
            // Buscar o curso pelo ID
            Curso curso = cursoRepository.findById(egressoDTO.getIdCurso())
                    .orElseThrow(() -> new RuntimeException(
                            "Curso com ID " + egressoDTO.getIdCurso() + " não encontrado"));

            // Criar e salvar o egresso
            Egresso egresso = new Egresso();
            egresso.setNome(egressoDTO.getNome());
            egresso.setEmail(egressoDTO.getEmail());
            egresso.setDescricao(egressoDTO.getDescricao());
            egresso.setFoto(egressoDTO.getFoto());
            egresso.setLinkedin(egressoDTO.getLinkedin());
            egresso.setInstagam(egressoDTO.getInstagam());
            egresso.setCurriculo(egressoDTO.getCurriculo());
            Egresso egressoSalvo = egressoRepository.save(egresso);

            // Criar e salvar a associação com o curso
            CursoEgresso cursoEgresso = new CursoEgresso();
            cursoEgresso.setEgresso(egressoSalvo);
            cursoEgresso.setCurso(curso);
            cursoEgresso.setAnoInicio(egressoDTO.getAnoInicio());
            cursoEgresso.setAnoFim(egressoDTO.getAnoFim());
            cursoEgressoRepository.save(cursoEgresso);

            return egressoSalvo;

        } catch (RuntimeException ex) {
            // Lançar exceção personalizada para curso não encontrado
            throw ex;
        } catch (Exception ex) {
            // Lançar exceção genérica para outros erros
            throw new RuntimeException("Erro ao criar egresso: " + ex.getMessage(), ex);
        }
    }



    // Buscar todos os egressos
    public List<Egresso> listarEgressos() {
        return egressoRepository.findAll();
    }

    // Buscar egresso pelo ID
    public Egresso buscarEgressoPorId(Integer id) {
        return egressoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Egresso não encontrado"));
    }

    // Atualizar um egresso existente
    public Egresso editarEgresso(Integer idEgresso, Egresso egressoAtualizado, Long idCurso, Integer anoInicio, Integer anoFim) {
        // Buscar o egresso existente
        Egresso egressoExistente = egressoRepository.findById(idEgresso)
                .orElseThrow(() -> new RuntimeException("Egresso não encontrado com ID: " + idEgresso));

        // Atualizar os campos do egresso
        egressoExistente.setNome(egressoAtualizado.getNome());
        egressoExistente.setEmail(egressoAtualizado.getEmail());
        egressoExistente.setDescricao(egressoAtualizado.getDescricao());
        egressoExistente.setFoto(egressoAtualizado.getFoto());
        egressoExistente.setLinkedin(egressoAtualizado.getLinkedin());
        egressoExistente.setInstagam(egressoAtualizado.getInstagam());
        egressoExistente.setCurriculo(egressoAtualizado.getCurriculo());

        // Atualizar ou criar a associação com o curso
        Curso curso = cursoRepository.findById(idCurso)
                .orElseThrow(() -> new RuntimeException("Curso não encontrado com ID: " + idCurso));

        CursoEgresso cursoEgresso = cursoEgressoRepository.findByEgressoAndCurso(egressoExistente, curso)
                .orElse(new CursoEgresso()); // Criar nova associação, se não existir

        cursoEgresso.setEgresso(egressoExistente);
        cursoEgresso.setCurso(curso);
        cursoEgresso.setAnoInicio(anoInicio);
        cursoEgresso.setAnoFim(anoFim);

        // Salvar a associação na tabela intermediária
        cursoEgressoRepository.save(cursoEgresso);

        // Salvar e retornar o egresso atualizado
        return egressoRepository.save(egressoExistente);
    }


    // Excluir um egresso pelo ID
    @Transactional
    public void excluirEgresso(Integer idEgresso) {
        // Verifica se o egresso existe
        Egresso egresso = egressoRepository.findById(idEgresso)
                .orElseThrow(() -> new RuntimeException("Egresso não encontrado com ID: " + idEgresso));

        // Remover os relacionamentos na tabela intermediária (CursoEgresso)
        cursoEgressoRepository.deleteByEgresso(egresso);

        // Excluir o egresso
        egressoRepository.delete(egresso);
    }

    // Listar egressos pelo nome
    public List<Egresso> listarEgressosPorNome(String nome) {
        return egressoRepository.findByNomeContaining(nome);
    }

    // Buscar egresso por email
    public Egresso buscarEgressoPorEmail(String email) {
        return egressoRepository.findByEmail(email);
    }

    // Listar egressos com LinkedIn associado
    public List<Egresso> listarEgressosComLinkedin() {
        return egressoRepository.findWithLinkedin();
    }

    // Listar egressos com currículo disponível
    public List<Egresso> listarEgressosComCurriculo() {
        return egressoRepository.findWithCurriculo();
    }

    //--------------------------------------

    //Obter Egressos por Curso, Ano
    public List<Egresso> obterEgressosPorCursoEAno(Integer idCurso, Integer anoInicio, Integer anoFim) {
        return cursoEgressoRepository.findEgressosByCursoAndAno(idCurso, anoInicio, anoFim);
    }

    //Obter Egressos por Cargo
    public List<Egresso> obterEgressosPorCargo(String descricao) {
        return cargoRepository.findEgressosByCargoDescricao(descricao);
    }

}
