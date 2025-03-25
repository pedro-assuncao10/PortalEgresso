package com.example.demo.service;

import com.example.demo.dto.CadastroRequestUsuario;
import com.example.demo.dto.CadastroResponseUsuario;
import com.example.demo.model.Usuario;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario autenticarUsuario(String login, String senha) {
        Optional<Usuario> usuario = usuarioRepository.findByLogin(login);

        if (usuario.isPresent() && usuario.get().getSenha().equals(senha)) {
            return usuario.get();
        } else {
            throw new RuntimeException("Login ou senha inválidos!!!.");
        }
    }

    public CadastroResponseUsuario cadastrarUsuario(CadastroRequestUsuario cadastroRequestUsuario) {
        Usuario usuario = new Usuario();
        usuario.setLogin(cadastroRequestUsuario.getLogin());
        usuario.setSenha(cadastroRequestUsuario.getSenha());  // Em produção, seria interessante usar criptografia
        usuario.setTipo(cadastroRequestUsuario.getTipo() != null ? cadastroRequestUsuario.getTipo() : "egresso");

        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        return new CadastroResponseUsuario("Usuário cadastrado com sucesso!", usuarioSalvo.getId());
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Usuario salvarUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    public void deletarUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }
}
