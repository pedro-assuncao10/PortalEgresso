package com.example.demo.service;

import com.example.demo.model.Curso;
import com.example.demo.model.CursoEgresso;
import com.example.demo.model.Egresso;
import com.example.demo.repository.CursoEgressoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CursoEgressoService {

    @Autowired
    private CursoEgressoRepository repository;

    public List<CursoEgresso> listarTodos() {
        return repository.findAll();
    }

    public Optional<CursoEgresso> buscarPorId(Integer id) {
        return repository.findById(id);
    }

    public CursoEgresso salvar(CursoEgresso cursoEgresso) {
        return repository.save(cursoEgresso);
    }

    public void excluir(Integer id) {
        repository.deleteById(id);
    }

    // 1. Buscar pelo ID de CursoEgresso
    public List<CursoEgresso> buscarPorIdCursoEgresso(Integer idCursoEgresso) {
        return repository.findByIdCursoEgresso(idCursoEgresso);
    }

    // 2. Buscar pelo Egresso associado
    public List<CursoEgresso> buscarPorEgresso(Egresso egresso) {
        return repository.findByEgresso(egresso);
    }

    // 3. Buscar pelo Curso associado
    public List<CursoEgresso> buscarPorCurso(Curso curso) {
        return repository.findByCurso(curso);
    }

    // 4. Buscar pelo anoInicio
    public List<CursoEgresso> buscarPorAnoInicio(Integer anoInicio) {
        return repository.findByAnoInicio(anoInicio);
    }

    // 5. Buscar por intervalo de anos (anoInicio e anoFim)
    public List<CursoEgresso> buscarPorAnoInicioEAnoFim(Integer anoInicio, Integer anoFim) {
        return repository.findByAnoInicioAndAnoFim(anoInicio, anoFim);
    }
}
