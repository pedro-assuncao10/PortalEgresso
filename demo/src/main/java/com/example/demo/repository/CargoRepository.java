package com.example.demo.repository;

import com.example.demo.model.Cargo;
import com.example.demo.model.Egresso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;

@Repository
public interface CargoRepository extends JpaRepository<Cargo, Integer> {
    List<Cargo> findByEgresso_IdEgresso(Integer idEgresso);

    // Consulta para buscar cargos de um egresso específico
    @Query("SELECT c FROM Cargo c WHERE c.egresso.idEgresso = :idEgresso")
    List<Cargo> findByEgressoId(@Param("idEgresso") Integer idEgresso);

    // Consulta para buscar cargos por local de trabalho
    @Query("SELECT c FROM Cargo c WHERE c.local LIKE %:local%")
    List<Cargo> findByLocalContaining(@Param("local") String local);

    // Consulta para buscar cargos em um intervalo de anos
    @Query("SELECT c FROM Cargo c WHERE c.anoInicio >= :anoInicio AND (c.anoFim <= :anoFim OR c.anoFim IS NULL)")
    List<Cargo> findByAnoRange(@Param("anoInicio") Integer anoInicio, @Param("anoFim") Integer anoFim);

    // Consulta para buscar cargos com descrição específica
    @Query("SELECT c FROM Cargo c WHERE c.descricao LIKE %:descricao%")
    List<Cargo> findByDescricaoContaining(@Param("descricao") String descricao);

    //Obter Egressos por Cargo
    @Query("SELECT c.egresso FROM Cargo c WHERE c.descricao = :descricao")
    List<Egresso> findEgressosByCargoDescricao(@Param("descricao") String descricao);
}
