package com.example.demo.repository;

import com.example.demo.model.Coordenador;
import com.example.demo.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;


import java.util.Optional;
import java.util.List;

@Repository
public interface CoordenadorRepository extends JpaRepository<Coordenador, Long> {
    Optional<Coordenador> findByLogin(String login);

    // Consulta para encontrar um coordenador pelo login
    @Query("SELECT c FROM Coordenador c WHERE c.login = :login")
    Coordenador fetchByLogin(@Param("login") String login);

    // Consulta para verificar se um coordenador existe pelo login e senha
    @Query("SELECT c FROM Coordenador c WHERE c.login = :login AND c.senha = :senha")
    Coordenador findByLoginAndSenha(@Param("login") String login, @Param("senha") String senha);

    // Consulta para buscar todos os coordenadores de um determinado tipo
    @Query("SELECT c FROM Coordenador c WHERE c.tipo = :tipo")
    List<Coordenador> findByTipo(@Param("tipo") String tipo);
}
