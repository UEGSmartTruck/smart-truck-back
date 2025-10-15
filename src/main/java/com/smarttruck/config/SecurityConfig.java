package com.smarttruck.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final WebConfig webConfig;

    public SecurityConfig(WebConfig webConfig) {
        this.webConfig = webConfig;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            // DESABILITA CSRF: A causa mais comum de 403 em POSTs.
            .csrf(AbstractHttpConfigurer::disable)

            // APLICA CORS: Usa a configuração do WebConfig.
            .cors(cors -> cors.configurationSource(webConfig.corsConfigurationSource()))

            // DEFINE REGRAS DE AUTORIZAÇÃO:
            .authorizeHttpRequests(authorize -> authorize
                // Libera TOTALMENTE o acesso ao H2 Console.
                .requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll()
                // Libera TOTALMENTE o acesso ao endpoint de login.
                .requestMatchers(AntPathRequestMatcher.antMatcher("/api/auth/login")).permitAll()
                // Para QUALQUER OUTRA requisição, precisa de autenticação.
                .anyRequest().authenticated()
            )

            // GARANTE API STATELESS: Não cria sessões.
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // Permite que o H2 Console seja renderizado (necessário por causa do .permitAll() acima)
            .headers(headers -> headers.frameOptions(options -> options.sameOrigin()))

            .build(); // Finaliza a construçãoaaa
    }
}
