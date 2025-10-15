// Caminho: src/main/java/com/smarttruck/config/WebConfig.java

package com.smarttruck.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class WebConfig {

    // Este método cria e expõe as regras de CORS como um Bean
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Define quais origens (frontends) podem acessar o backend
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));

        // Define quais métodos HTTP são permitidos (GET, POST, etc.)
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Permite todos os cabeçalhos na requisição
        configuration.setAllowedHeaders(Arrays.asList("*"));

        // Permite o envio de credenciais (como cookies)
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Aplica esta configuração para TODAS as rotas da sua API
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
