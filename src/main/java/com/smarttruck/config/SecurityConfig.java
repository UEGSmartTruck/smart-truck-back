package com.smarttruck.config;

import com.smarttruck.shared.security.JwtAuthenticationFilter;
import com.smarttruck.shared.security.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable() // desabilita CSRF para APIs REST
            .authorizeHttpRequests()
            // ALTERAÇÃO AQUI: Use o caminho do endpoint *sem* o context path (/api)
            .requestMatchers("/login").permitAll() // ✅ Libera apenas /login
            // .requestMatchers("/users").permitAll()
            // Se a regra anterior falhar, você também pode tentar liberar *tudo* sob /api/login:
            // .requestMatchers("/api/login").permitAll() // MANTENHA se remover o context-path

            .anyRequest().authenticated().and().sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // sem sessão
            .and().addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
                UsernamePasswordAuthenticationFilter.class); // filtro JWT

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
