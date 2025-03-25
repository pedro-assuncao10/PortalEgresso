package com.example.demo.repository;

import com.example.demo.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.List;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {
    Optional<Curso> findByNome(String nome);

    // Consulta para encontrar cursos pelo nome
    @Query("SELECT c FROM Curso c WHERE c.nome LIKE %:nome%")
    List<Curso> findByNomeContaining(@Param("nome") String nome);

    // Consulta para encontrar cursos por nível
    @Query("SELECT c FROM Curso c WHERE c.nivel = :nivel")
    List<Curso> findByNivel(@Param("nivel") String nivel);

    // Consulta para encontrar todos os cursos de um coordenador específico
    @Query("SELECT c FROM Curso c WHERE c.coordenador.idCoordenador = :idCoordenador")
    List<Curso> findByCoordenadorId(@Param("idCoordenador") Long idCoordenador);

    // Consulta para contar o número de cursos por coordenador
    @Query("SELECT COUNT(c) FROM Curso c WHERE c.coordenador.idCoordenador = :idCoordenador")
    Long countByCoordenadorId(@Param("idCoordenador") Long idCoordenador);
}