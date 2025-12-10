package com.smarttruck.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smarttruck.shared.security.JwtAuthenticationFilter;
import com.smarttruck.shared.security.JwtTokenProvider;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    public SecurityConfig(final JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            final com.smarttruck.presentation.dto.ErrorResponse error = new com.smarttruck.presentation.dto.ErrorResponse(
                "Acesso não autorizado. Faça login para continuar.",
                java.time.Instant.now(),
                new com.smarttruck.presentation.dto.ErrorResponse.ErrorDetails("UNAUTHORIZED", "Authentication required")
            );
            response.getWriter().write(new ObjectMapper().writeValueAsString(error));
        };
    }

    /**
     * Define a configuração principal de segurança da aplicação.
     */
    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
        configureStatelessnessAndCsrf(http);
        configureAuthorization(http);
        addJwtFilter(http);
        http.exceptionHandling(e -> e.authenticationEntryPoint(authenticationEntryPoint()));
        return http.build();
    }

    /**
     * Bean para codificar senhas com BCrypt.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Bean do filtro JWT — facilita o mock e a injeção em testes.
     */
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(
        final JwtTokenProvider jwtTokenProvider) {
        return new JwtAuthenticationFilter(jwtTokenProvider);
    }

    private void configureStatelessnessAndCsrf(final HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.setSharedObject(WebMvcConfigurer.class, corsConfigurationSource());
        http.sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    }

    private void configureAuthorization(final HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(
            auth -> auth.requestMatchers("/**").permitAll().anyRequest().authenticated());
    }

    private void addJwtFilter(final HttpSecurity http) throws Exception {
        http.addFilterBefore(jwtAuthenticationFilter(jwtTokenProvider),
            UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public WebMvcConfigurer corsConfigurationSource() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(final CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*").allowedMethods("*")
                    .allowedHeaders("*");
            }
        };
    }
}
