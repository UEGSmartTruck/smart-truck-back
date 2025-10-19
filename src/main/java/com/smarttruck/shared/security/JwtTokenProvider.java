package com.smarttruck.shared.security;

import java.security.Key;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.smarttruck.shared.token.TokenBlacklist;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {

    public static final long ACCESS_TOKEN_VALIDITY_IN_MS = 3600000; // 1 hora
    private final Key secretKey;

    @Autowired
    TokenBlacklist tokenBlacklist; // package-private para testes

    void setTokenBlacklist(final TokenBlacklist tokenBlacklist) {
        this.tokenBlacklist = tokenBlacklist;
    }

    public JwtTokenProvider(@Value("${jwt.secret:}") final String jwtSecret) {
        if (jwtSecret == null || jwtSecret.isBlank()) {
            this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        } else {
            this.secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        }
    }

    public String generateToken(final String userId, final String email, final String name) {
        final Date now = new Date();
        final Date expiry = new Date(now.getTime() + ACCESS_TOKEN_VALIDITY_IN_MS);

        return Jwts.builder().setSubject(userId).claim("email", email).claim("name", name)
                .setIssuedAt(now).setExpiration(expiry)
                .signWith(secretKey, SignatureAlgorithm.HS256).compact();
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
        final Claims claims = Jwts.parserBuilder().setSigningKey(secretKey).build()
                .parseClaimsJws(token).getBody();
        return claims.get("email", String.class);
    }

    public void invalidateToken(final String token) {
        tokenBlacklist.addToBlacklist(token);
    }
}
