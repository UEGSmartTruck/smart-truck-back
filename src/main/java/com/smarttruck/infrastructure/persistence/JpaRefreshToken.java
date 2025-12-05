package com.smarttruck.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

/**
 * Entidade JPA que representa a tabela de refresh tokens no banco de dados. &lt;p&gt; Esta classe é
 * usada internamente pelo adaptador Spring Data para mapear os campos entre a camada de
 * persistência e o domínio.
 */
@Entity
@Table(name = "refresh_tokens")
public class JpaRefreshToken {

    @Id
    @Column(length = 36)
    private String token;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected JpaRefreshToken() {
        // Construtor necessário para JPA
    }

    public JpaRefreshToken(final String token, final String userId) {
        this.token = token;
        this.userId = userId;
        this.createdAt = Instant.now();
    }

    public String getToken() {
        return token;
    }

    public String getUserId() {
        return userId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
