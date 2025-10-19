package com.smarttruck.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.smarttruck.shared.security.JwtAuthenticationFilter;
import com.smarttruck.shared.security.JwtTokenProvider;

@Configuration
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    public SecurityConfig(final JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * Define a configuração principal de segurança da aplicação.
     */
    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
        configureStatelessnessAndCsrf(http);
        configureAuthorization(http);
        addJwtFilter(http);

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
                auth -> auth.requestMatchers("/login").permitAll().anyRequest().authenticated());
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
