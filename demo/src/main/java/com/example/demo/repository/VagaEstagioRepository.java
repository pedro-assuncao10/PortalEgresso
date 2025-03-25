package com.example.demo.repository;

import com.example.demo.model.VagaEstagio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;


@Repository
public interface VagaEstagioRepository extends JpaRepository<VagaEstagio, Long> {

    List<VagaEstagio> findByEgresso_IdEgresso(Integer idEgresso);

    // Busca vagas pelo ID do curso associado
    @Query("SELECT v FROM VagaEstagio v JOIN v.cursosDestinados c WHERE c.idCurso = :cursoId")
    List<VagaEstagio> findByCursoId(@Param("cursoId") Long cursoId);


    // Busca vagas pelo tipo de estágio
    List<VagaEstagio> findByTipoEstagio(String tipoEstagio);

    // Busca vagas pela localidade
    List<VagaEstagio> findByLocalidade(String localidade);

    // Busca vagas ordenadas pela data de publicação (recentes primeiro)
    List<VagaEstagio> findAllByOrderByDataPublicacaoDesc();

    // Verifica se um egresso tem permissão para editar uma vaga específica
    //boolean existsByIdVagaAndEgressoIdEgresso(Long idVaga, Long idEgresso);
}
