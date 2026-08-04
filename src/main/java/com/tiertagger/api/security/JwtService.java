package com.tiertagger.api.security;

import com.tiertagger.api.config.AppProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.UUID;

/**
 * Serviço de geração/validação de tokens JWT (access e refresh).
 */
@Service
public class JwtService {

    private final AppProperties properties;
    private final SecretKey signingKey;

    public JwtService(AppProperties properties) {
        this.properties = properties;
        this.signingKey = Keys.hmacShaKeyFor(
                properties.getJwt().getSecret().getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(Long userId, String username, String role) {
        Date now = new Date();
        long ttlMillis = properties.getJwt().getAccessTtlMinutes() * 60_000L;
        Date expiry = new Date(now.getTime() + ttlMillis);

        return Jwts.builder()
                .subject(username)
                .claim("uid", userId)
                .claim("role", role)
                .claim("type", "access")
                .issuedAt(now)
                .expiration(expiry)
                .signWith(signingKey)
                .compact();
    }

    public String generateRefreshToken(Long userId, String username, String role) {
        Date now = new Date();
        long ttlMillis = properties.getJwt().getRefreshTtlDays() * 86_400_000L;
        Date expiry = new Date(now.getTime() + ttlMillis);

        return Jwts.builder()
                .subject(username)
                .claim("uid", userId)
                .claim("role", role)
                .claim("type", "refresh")
                .id(UUID.randomUUID().toString())
                .issuedAt(now)
                .expiration(expiry)
                .signWith(signingKey)
                .compact();
    }

    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isTokenValid(String token, String expectedType) {
        try {
            Claims claims = parseToken(token);
            return expectedType.equals(claims.get("type", String.class))
                    && claims.getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public long getAccessTtlSeconds() {
        return properties.getJwt().getAccessTtlMinutes() * 60L;
    }

    public long getRefreshTtlDays() {
        return properties.getJwt().getRefreshTtlDays();
    }

    public OffsetDateTime getRefreshExpiry() {
        return OffsetDateTime.now().plusDays(properties.getJwt().getRefreshTtlDays());
    }
}
