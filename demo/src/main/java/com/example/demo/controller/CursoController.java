package com.example.demo.controller;

import com.example.demo.dto.CursoRequest;
import com.example.demo.dto.CursoResponse;
import com.example.demo.service.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cursos") // Mapeia a URL base para os cursos
public class CursoController {

    @Autowired
    private CursoService cursoService;

    /**
     * Endpoint para listar todos os cursos.
     */
    @GetMapping
    public ResponseEntity<?> listarCursos() {
        try {
            List<CursoResponse> cursos = cursoService.listarCursos();
            return new ResponseEntity<>(cursos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Erro ao listar cursos: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint para buscar um curso pelo ID enviado no corpo da requisição.
     */
    @PostMapping("/buscar")
    public ResponseEntity<?> buscarCursoPorId(@RequestBody Map<String, Long> request) {
        Long idCurso = request.get("idCurso"); // Extrai o ID do corpo
        try {
            CursoResponse curso = cursoService.buscarCursoPorId(idCurso);
            if (curso == null) {
                return new ResponseEntity<>("Curso não encontrado com ID: " + idCurso, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(curso, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Erro ao buscar curso: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint para cadastrar um novo curso.
     */
    @PostMapping
    public ResponseEntity<?> cadastrarCurso(@RequestBody CursoRequest cursoRequest) {
        try {
            cursoService.cadastrarCurso(cursoRequest);
            CursoResponse cursoResponse = cursoService.buscarCursoPorNome(cursoRequest.getNome());
            return new ResponseEntity<>("Curso cadastrado com sucesso: " + cursoResponse, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Erro ao cadastrar curso: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint para atualizar um curso existente pelo ID enviado no corpo da requisição.
     */
    @PutMapping
    public ResponseEntity<?> atualizarCurso(@RequestBody Map<String, Object> request) {
        Long idCurso = ((Number) request.get("idCurso")).longValue(); // Conversão segura // Extrai o ID do curso
        CursoRequest cursoRequest = new CursoRequest();
        cursoRequest.setNome((String) request.get("nome"));
        cursoRequest.setNivel((String) request.get("nivel"));
        Long idCoordenador = (request.get("idCoordenador") instanceof Number)
                ? ((Number) request.get("idCoordenador")).longValue()
                : null;  // Ou um valor padrão, caso não seja um número
        cursoRequest.setIdCoordenador(idCoordenador);


        try {
            CursoResponse cursoAtualizado = cursoService.atualizarCurso(idCurso, cursoRequest);
            if (cursoAtualizado == null) {
                return new ResponseEntity<>("Curso não encontrado com ID: " + idCurso, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(cursoAtualizado, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Erro ao atualizar curso: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint para deletar um curso pelo ID enviado no corpo da requisição.
     */
    @DeleteMapping
    public ResponseEntity<?> deletarCurso(@RequestBody Map<String, Long> request) {
        Long idCurso = request.get("idCurso"); // Extrai o ID do corpo
        try {
            cursoService.deletarCurso(idCurso); // Chama o método de deleção
            return new ResponseEntity<>("Curso deletado com sucesso", HttpStatus.NO_CONTENT); // Retorna 204 (No Content) em caso de sucesso
        } catch (Exception e) {
            return new ResponseEntity<>("Erro ao deletar curso: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); // Em caso de erro, retorna 500
        }
    }

}
