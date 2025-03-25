package com.example.demo.repository;

import com.example.demo.model.Egresso;
import com.example.demo.dto.EgressoCargoCursoDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;


import java.util.List;

@Repository
public interface EgressoRepository extends JpaRepository<Egresso, Integer> {
    // Consulta para buscar egressos pelo nome (contém uma string)
    @Query("SELECT e FROM Egresso e WHERE e.nome LIKE %:nome%")
    List<Egresso> findByNomeContaining(@Param("nome") String nome);

    // Consulta para buscar egresso pelo email
    @Query("SELECT e FROM Egresso e WHERE e.email = :email")
    Egresso findByEmail(@Param("email") String email);

    // Consulta para buscar egressos com um LinkedIn associado
    @Query("SELECT e FROM Egresso e WHERE e.linkedin IS NOT NULL")
    List<Egresso> findWithLinkedin();

    // Consulta para buscar egressos com currículo disponível
    @Query("SELECT e FROM Egresso e WHERE e.curriculo IS NOT NULL")
    List<Egresso> findWithCurriculo();

    //------------------------------------------------------------------
/*
    @Query("SELECT new com.example.demo.dto.EgressoCargoCursoDTO(" +
            "e.nome, e.email, e.descricao, " +
            "c.descricao, c.local, c.anoInicio, c.anoFim, " +
            "cu.nome, cu.nivel, ce.anoInicio, ce.anoFim) " +
            "FROM Egresso e " +
            "JOIN Cargo c ON e.idEgresso = c.idEgresso " +
            "JOIN CursoEgresso ce ON e.idEgresso = ce.idEgresso " +
            "JOIN Curso cu ON ce.idCurso = cu.idCurso")
    List<EgressoCargoCursoDTO> findEgressoCargoCurso();
    // Ou se você precisar de um filtro, como por nome do egresso:
    @Query("SELECT new com.example.demo.dto.EgressoCargoCursoDTO(" +
            "e.nome, e.email, e.descricao, " +
            "c.descricao, c.local, c.anoInicio, c.anoFim, " +
            "cu.nome, cu.nivel, ce.anoInicio, ce.anoFim) " +
            "FROM Egresso e " +
            "JOIN Cargo c ON e.idEgresso = c.idEgresso " +
            "JOIN CursoEgresso ce ON e.idEgresso = ce.idEgresso " +
            "JOIN Curso cu ON ce.idCurso = cu.idCurso " +
            "WHERE e.nome = :nome")
    List<EgressoCargoCursoDTO> findEgressoCargoCursoByName(@Param("nome") String nome);
*/
}
