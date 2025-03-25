package com.example.demo.repository;

import com.example.demo.model.CursoEgresso;
import com.example.demo.model.Egresso;
import com.example.demo.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.List;

@Repository
public interface CursoEgressoRepository extends JpaRepository<CursoEgresso, Integer> {

    List<CursoEgresso> findAll();

    Optional<CursoEgresso> findByEgressoAndCurso(Egresso egresso, Curso curso);

    void deleteByEgresso(Egresso egresso);

    // Buscar pelo ID de CursoEgresso
    List<CursoEgresso> findByIdCursoEgresso(Integer idCursoEgresso);

    // Buscar pelo Egresso associado
    List<CursoEgresso> findByEgresso(Egresso egresso);

    //  Buscar pelo Curso associado
    List<CursoEgresso> findByCurso(Curso curso);

    // Buscar pelo anoInicio
    List<CursoEgresso> findByAnoInicio(Integer anoInicio);

    // Buscar por intervalo de anos (anoInicio e anoFim)
    List<CursoEgresso> findByAnoInicioAndAnoFim(Integer anoInicio, Integer anoFim);

    //Obter Egressos por Curso, Ano
    @Query("SELECT ce.egresso FROM CursoEgresso ce WHERE ce.curso.idCurso = :idCurso AND ce.anoInicio = :anoInicio AND ce.anoFim = :anoFim")
    List<Egresso> findEgressosByCursoAndAno(@Param("idCurso") Integer idCurso, @Param("anoInicio") Integer anoInicio, @Param("anoFim") Integer anoFim);

}
