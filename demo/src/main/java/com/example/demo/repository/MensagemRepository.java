package com.example.demo.repository;

import com.example.demo.model.Mensagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MensagemRepository extends JpaRepository<Mensagem, Long> {
    List<Mensagem> findByGrupoIdOrderByDataEnvioAsc(Long grupoId);
}
