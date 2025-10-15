package com.smarttruck.shared.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final Key secretKey = Keys.hmacShaKeyFor("MinhaChaveSecretaMuitoLongaParaJWT1234567890".getBytes());
    private final long validityInMs = 3600000; // 1 hora

    public String generateToken(String userId, String email) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + validityInMs);

        return Jwts.builder()
            .setSubject(userId)
            .claim("email", email)
            .setIssuedAt(now)
            .setExpiration(expiry)
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
    }

}
