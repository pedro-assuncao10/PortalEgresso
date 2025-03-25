package com.example.demo.repository;

import com.example.demo.model.Depoimento;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Date;

@Repository
public interface DepoimentoRepository extends JpaRepository<Depoimento, Integer> {
    List<Depoimento> findByEgresso_IdEgresso(Integer idEgresso);

    // Consulta para buscar depoimentos de um egresso específico
    @Query("SELECT d FROM Depoimento d WHERE d.egresso.idEgresso = :idEgresso")
    List<Depoimento> findByEgressoId(@Param("idEgresso") Integer idEgresso);

    // Consulta para buscar depoimentos por texto (contém uma string)
    @Query("SELECT d FROM Depoimento d WHERE d.texto LIKE %:texto%")
    List<Depoimento> findByTextoContaining(@Param("texto") String texto);

    // Consulta para buscar depoimentos em um intervalo de datas
    @Query("SELECT d FROM Depoimento d WHERE d.data BETWEEN :startDate AND :endDate")
    List<Depoimento> findByDateRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    // Consulta para buscar depoimentos mais recentes
    @Query("SELECT d FROM Depoimento d ORDER BY d.data DESC")
    List<Depoimento> findLatestDepoimentos();
}
