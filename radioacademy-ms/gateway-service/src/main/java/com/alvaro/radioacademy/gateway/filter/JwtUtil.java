package com.alvaro.radioacademy.gateway.filter;

import com.alvaro.radioacademy.shared.util.RSAKeyUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.PublicKey;

@Component
public class JwtUtil {
    private final PublicKey publicKey;

    public JwtUtil(@Value("${app.jwt.public-key}") Resource publicKeyResource) {
        try {
            String keyStr = new String(publicKeyResource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            this.publicKey = RSAKeyUtils.getPublicKey(keyStr);
        } catch (Exception e) {
            throw new RuntimeException("Error al cargar la llave pública en el Gateway", e);
        }
    }

    public void validateToken(final String token) {
        Jwts.parserBuilder().setSigningKey(publicKey).build().parseClaimsJws(token);
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(publicKey).build().parseClaimsJws(token).getBody();
    }
}