package com.example.demo.service;

import com.example.demo.dto.CadastroRequest;
import com.example.demo.dto.CadastroResponse;
import com.example.demo.model.Coordenador;
import com.example.demo.repository.CoordenadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import jakarta.annotation.PostConstruct;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import lombok.extern.slf4j.Slf4j;



import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CoordenadorService {

    @Autowired
    private CoordenadorRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * Autentica um coordenador pelo login e senha.
     *
     * @param login Login do coordenador.
     * @param senha Senha do coordenador.
     * @return Coordenador autenticado.
     */
    public Coordenador autenticar(String login, String senha) {
        System.out.println("Tentando autenticar usuário: " + login);

        Optional<Coordenador> optionalCoordenador = repository.findByLogin(login);
        if (optionalCoordenador.isEmpty()) {
            System.out.println("⚠️ Usuário não encontrado: " + login);
            throw new RuntimeException("Usuário não encontrado");
        }

        Coordenador coordenador = optionalCoordenador.get();
        System.out.println("✅ Usuário encontrado: " + coordenador.getLogin());
        System.out.println("🔐 Senha armazenada (hash Bcrypt): " + coordenador.getSenha());

        if (!passwordEncoder.matches(senha, coordenador.getSenha())) {
            System.out.println("🚫 Senha incorreta para usuário: " + login);
            throw new RuntimeException("Senha incorreta");
        }

        System.out.println("🎉 Usuário autenticado com sucesso: " + login);
        return coordenador;
    }




    /**
     * Cadastra um novo coordenador.
     *
     * @param cadastroRequest Dados do cadastro.
     * @return Resposta com mensagem de sucesso e ID do coordenador cadastrado.
     */
    public CadastroResponse cadastrar(CadastroRequest cadastroRequest) {
        if (repository.findByLogin(cadastroRequest.getLogin()).isPresent()) {
            throw new RuntimeException("Login já cadastrado!");
        }

        Coordenador coordenador = new Coordenador();
        coordenador.setLogin(cadastroRequest.getLogin());

        // Agora a senha será armazenada com criptografia
        coordenador.setSenha(passwordEncoder.encode(cadastroRequest.getSenha()));
        coordenador.setTipo(cadastroRequest.getTipo());

        Coordenador coordenadorSalvo = repository.save(coordenador);
        return new CadastroResponse("Usuário cadastrado com sucesso!", coordenadorSalvo.getIdCoordenador());
    }

    public List<Coordenador> listarTodos() {
        return repository.findAll();
    }


    public Optional<Coordenador> buscarPorId(Long id) { // Alterado para Long
        return repository.findById(id);
    }


    public Coordenador salvar(Coordenador coordenador) {
        return repository.save(coordenador);
    }


    public void excluir(Long id) { // Alterado para Long
        repository.deleteById(id);
    }

    /**
     * Busca um coordenador pelo login.
     *
     * @param login Login do coordenador.
     * @return Coordenador encontrado.
     */
    public Coordenador buscarPorLogin(String login) {
        Coordenador coordenador = repository.fetchByLogin(login);
        if (coordenador == null) {
            throw new RuntimeException("Coordenador com login " + login + " não encontrado!");
        }
        return coordenador;
    }

    /**
     * Verifica se um coordenador existe pelo login e senha.
     *
     * @param login Login do coordenador.
     * @param senha Senha do coordenador.
     * @return Coordenador encontrado ou null.
     */
    public Coordenador verificarPorLoginESenha(String login, String senha) {
        return repository.findByLoginAndSenha(login, senha);
    }

    /**
     * Busca todos os coordenadores de um determinado tipo.
     *
     * @param tipo Tipo do coordenador.
     * @return Lista de coordenadores do tipo especificado.
     */
    public List<Coordenador> buscarPorTipo(String tipo) {
        return repository.findByTipo(tipo);
    }
}
