package com.example.demo.service;

import com.example.demo.dto.CursoRequest;
import com.example.demo.dto.CursoResponse;
import com.example.demo.model.Coordenador;
import com.example.demo.model.Curso;
import com.example.demo.repository.CoordenadorRepository;
import com.example.demo.repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CursoService {

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private CoordenadorRepository coordenadorRepository;

    /**
     * Listar todos os cursos.
     */
    public List<CursoResponse> listarCursos() {
        return cursoRepository.findAll().stream()
                .map(this::toCursoResponse)
                .collect(Collectors.toList());
    }

    /**
     * Cadastrar um novo curso.
     */
    public void cadastrarCurso(CursoRequest cursoRequest) {
        // Criar o curso a ser salvo
        Curso curso = new Curso();
        curso.setNome(cursoRequest.getNome());
        curso.setNivel(cursoRequest.getNivel());

        // Buscar o coordenador pelo ID
        Coordenador coordenador = coordenadorRepository.findById(cursoRequest.getIdCoordenador())
                .orElseThrow(() -> new RuntimeException("Coordenador não encontrado"));

        // Associa o coordenador ao curso
        curso.setCoordenador(coordenador);

        // Salva o curso no banco de dados
        cursoRepository.save(curso);
    }

    /**
     * Buscar um curso por ID.
     */
    public CursoResponse buscarCursoPorId(Long id) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Curso não encontrado com ID: " + id));
        return toCursoResponse(curso);
    }

    /**
     * Atualizar um curso existente.
     */
    public CursoResponse atualizarCurso(Long id, CursoRequest cursoRequest) {
        // Busca o curso pelo ID
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Curso não encontrado com ID: " + id));

        // Busca o coordenador pelo ID fornecido no cursoRequest
        Coordenador coordenador = coordenadorRepository.findById(cursoRequest.getIdCoordenador())
                .orElseThrow(() -> new RuntimeException("Coordenador não encontrado com ID: " + cursoRequest.getIdCoordenador()));

        // Atualiza os dados do curso
        curso.setNome(cursoRequest.getNome());
        curso.setNivel(cursoRequest.getNivel());
        curso.setCoordenador(coordenador); // Atribui o coordenador correto

        // Salva o curso atualizado no banco de dados
        Curso atualizado = cursoRepository.save(curso);
        return toCursoResponse(atualizado);
    }


    /**
     * Deletar um curso pelo ID.
     */
    public void deletarCurso(Long id) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Curso não encontrado com ID: " + id));
        cursoRepository.delete(curso);
    }

    /**
     * Converter Curso para CursoResponse (DTO).
     */
    private CursoResponse toCursoResponse(Curso curso) {
        CursoResponse response = new CursoResponse();
        response.setIdCurso(curso.getIdCurso());
        response.setNome(curso.getNome());
        response.setNivel(curso.getNivel());
        response.setIdCoordenador(curso.getCoordenador().getIdCoordenador()); // Usando o ID do coordenador
        return response;
    }

    public CursoResponse buscarCursoPorNome(String nome) {
        Curso curso = cursoRepository.findByNome(nome)
                .orElseThrow(() -> new RuntimeException("Curso não encontrado com o nome: " + nome));
        return toCursoResponse(curso);
    }


    /**
     * Buscar cursos por nível.
     */
    public List<CursoResponse> buscarCursosPorNivel(String nivel) {
        return cursoRepository.findByNivel(nivel).stream()
                .map(this::toCursoResponse)
                .collect(Collectors.toList());
    }

    /**
     * Buscar todos os cursos de um coordenador específico.
     */
    public List<CursoResponse> buscarCursosPorCoordenador(Long idCoordenador) {
        return cursoRepository.findByCoordenadorId(idCoordenador).stream()
                .map(this::toCursoResponse)
                .collect(Collectors.toList());
    }

    /**
     * Contar o número de cursos por coordenador.
     */
    public Long contarCursosPorCoordenador(Long idCoordenador) {
        return cursoRepository.countByCoordenadorId(idCoordenador);
    }
}
