package com.smarttruck.domain.repository;

/**
 * Porta de repositório para persistência de refresh tokens no domínio. &lt;p&gt; Implementações
 * podem ser em memória (desenvolvimento) ou baseada em JPA para produção.
 */
public interface RefreshTokenRepository {
    /**
     * Persiste um refresh token associado a um usuário.
     *
     * @param refreshToken token a ser persistido
     * @param userId       id do usuário associado ao token
     */
    void save(String refreshToken, String userId);

    /**
     * Busca o userId associado a um refresh token.
     *
     * @param refreshToken token a buscar
     * @return id do usuário associado ou null se não encontrado
     */
    String findUserIdByRefreshToken(String refreshToken);

    /**
     * Remove um refresh token do repositório.
     *
     * @param refreshToken token a ser removido
     */
    void remove(String refreshToken);
}
