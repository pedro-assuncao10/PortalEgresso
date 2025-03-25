package com.example.demo.security;

import com.example.demo.model.Coordenador;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private final Coordenador coordenador;

    public CustomUserDetails(Coordenador coordenador) {
        this.coordenador = coordenador;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + coordenador.getTipo().toUpperCase()));
    }

    @Override
    public String getPassword() {
        return coordenador.getSenha();
    }

    @Override
    public String getUsername() {
        return coordenador.getLogin();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
