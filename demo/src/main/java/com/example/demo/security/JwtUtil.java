package com.example.demo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component // 🔹 Registra essa classe como um Bean do Spring
public class JwtUtil {

    // 🔥 Gera uma chave secreta segura para HS256 (pelo menos 256 bits)
    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // 📌 Gera um token JWT válido por 1 hora
    public String generateToken(String login) {
        return Jwts.builder()
                .setSubject(login)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hora de validade
                .signWith(SECRET_KEY) // 🔥 Usa a chave segura
                .compact();
    }

    // 📌 Valida o token e retorna as informações contidas nele
    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 📌 Verifica se o token é válido
    public boolean isTokenValid(String token, String login) {
        String tokenLogin = extractClaims(token).getSubject();
        return (tokenLogin.equals(login) && !isTokenExpired(token));
    }

    // 📌 Verifica se o token expirou
    public boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }
}
