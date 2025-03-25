package com.example.demo.repository;

import com.example.demo.model.GrupoDiscussao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GrupoDiscussaoRepository extends JpaRepository<GrupoDiscussao, Long> {
}
