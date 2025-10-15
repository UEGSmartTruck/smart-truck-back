package com.smarttruck.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    // Encoder de senhas
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Configuração de segurança HTTP
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Desativa CSRF apenas para as rotas da API
            .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"))

            // Define as regras de autorização
            .authorizeHttpRequests(
                auth -> auth.requestMatchers("/api/login").permitAll()  // login público
                    .requestMatchers("/api/**")
                    .permitAll()     // demais endpoints da API liberados (ajuste conforme necessário)
                    .anyRequest().authenticated()               // o restante requer autenticação
            );

        return http.build();
    }
}
