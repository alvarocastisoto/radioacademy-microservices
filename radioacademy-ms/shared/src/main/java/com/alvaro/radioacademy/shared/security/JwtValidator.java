package com.alvaro.radioacademy.shared.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.security.PublicKey;
import java.util.Date;
import java.util.function.Function;

public class JwtValidator {

    private final PublicKey publicKey;

    public JwtValidator(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(publicKey) // Validamos con la CLAVE PÚBLICA
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenValid(String token) {
        return !extractClaim(token, Claims::getExpiration).before(new Date());
    }
}