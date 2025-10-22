package com.smarttruck.infrastructure.persistence;

import com.smarttruck.domain.repository.RefreshTokenRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Adaptador que usa Spring Data JPA para persistir refresh tokens no banco relacional. &lt;p&gt;
 * Esta implementação delega ao repositório JPA gerado (`SpringDataJpaRefreshTokenRepository`) após
 * converter os dados através do mapper.
 */
@Repository
public class JpaRefreshTokenRepository implements RefreshTokenRepository {
    private final SpringDataJpaRefreshTokenRepository repository;
    private final JpaRefreshTokenMapper mapper;

    public JpaRefreshTokenRepository(final SpringDataJpaRefreshTokenRepository repository,
                                     final JpaRefreshTokenMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public void save(final String refreshToken, final String userId) {
        final JpaRefreshToken entity = mapper.toEntity(refreshToken, userId);
        if (entity == null) {
            throw new NullPointerException(
                "Mapper returned null entity for refreshToken: " + refreshToken);
        }
        repository.save(entity);
    }

    @Override
    public String findUserIdByRefreshToken(final String refreshToken) {
        final Optional<JpaRefreshToken> found = repository.findById(refreshToken);
        return found.map(JpaRefreshToken::getUserId).orElse(null);
    }

    @Override
    public void remove(final String refreshToken) {
        repository.deleteById(refreshToken);
    }
}
