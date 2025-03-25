package com.example.demo.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        // 🔥 Permitir credenciais (cookies, autenticação)
        config.setAllowCredentials(true);

        // 🔥 Permitir apenas origens específicas para maior segurança
        config.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://localhost:8080"));

        // 🔥 Permitir métodos HTTP
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // 🔥 Permitir cabeçalhos específicos
        config.setAllowedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Authorization"));

        // 🔥 Permitir expor certos headers na resposta
        config.setExposedHeaders(Arrays.asList("Authorization", "Content-Type"));

        // 🔥 Corrigir erro de preflight bloqueado
        config.setMaxAge(3600L); // Tempo de cache para preflight requests

        // 🔥 Aplicar a configuração para todas as rotas da API
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", config);

        return new CorsFilter(source);
    }
}
