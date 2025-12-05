package com.smarttruck.shared.security;

import com.smarttruck.shared.token.TokenBlacklist;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    public static final long ACCESS_TOKEN_VALIDITY_IN_MS = 3600000; // 1 hora
    private final Key secretKey;

    @Autowired
    TokenBlacklist tokenBlacklist;

    public JwtTokenProvider(@Value("${jwt.secret:}") final String jwtSecret) {
        if (jwtSecret == null || jwtSecret.isBlank()) {
            this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        } else {
            this.secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        }
    }

    void setTokenBlacklist(final TokenBlacklist tokenBlacklist) {
        this.tokenBlacklist = tokenBlacklist;
    }

    public String generateToken(final String userId, final String email, final String name) {
        final Date now = new Date();
        final Date expiry = new Date(now.getTime() + ACCESS_TOKEN_VALIDITY_IN_MS);

        return Jwts.builder().setSubject(userId).claim("email", email).claim("name", name)
            .setIssuedAt(now).setExpiration(expiry).signWith(secretKey, SignatureAlgorithm.HS256)
            .compact();
    }

    public boolean validateToken(final String token) {
        if (tokenBlacklist.isBlacklisted(token)) {
            return false;
        }

        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (final Exception e) {
            return false;
        }
    }

    public String getEmailFromToken(final String token) {
        final Claims claims =
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
        return claims.get("email", String.class);
    }

    public String getUserIdFromToken(final String token) {
        final Claims claims =
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public void invalidateToken(final String token) {
        tokenBlacklist.addToBlacklist(token);
    }

    /**
     * Verifica se o token deve ser renovado (menos de 15 minutos para expirar).
     *
     * @param token JWT token a ser verificado
     * @return true se o token deve ser renovado (remaining < 15 minutos), false caso contrário
     */
    public boolean shouldRefreshToken(final String token) {
        try {
            final Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

            final Date expiration = claims.getExpiration();
            final long now = System.currentTimeMillis();
            final long remaining = expiration.getTime() - now;

            // 15 minutos = 900000 milliseconds
            return remaining < 900000 && remaining > 0;
        } catch (final Exception e) {
            return false;
        }
    }

    /**
     * Gera um novo token com as mesmas claims do token original.
     *
     * @param token JWT token original a ser renovado
     * @return novo JWT token com expiration atualizada
     */
    public String refreshToken(final String token) {
        final Claims claims = Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .getBody();

        final String userId = claims.getSubject();
        final String email = claims.get("email", String.class);
        final String name = claims.get("name", String.class);

        return generateToken(userId, email, name);
    }
}
