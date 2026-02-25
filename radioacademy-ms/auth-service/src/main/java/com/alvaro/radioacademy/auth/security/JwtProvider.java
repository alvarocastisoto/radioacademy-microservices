package com.alvaro.radioacademy.auth.security;

import com.alvaro.radioacademy.shared.util.RSAKeyUtils; // Tu clase del módulo shared
import com.alvaro.radioacademy.auth.entity.User; // Tu entidad User
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;

import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.util.Date;

@Component
public class JwtProvider {

    @Value("${app.jwt.private-key}")
    private Resource privateKeyResource;

    private PrivateKey privateKey;

    @PostConstruct
    public void init() {
        try {
            String keyStr = new String(privateKeyResource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            this.privateKey = RSAKeyUtils.getPrivateKey(keyStr);
        } catch (Exception e) {
            throw new RuntimeException("Error al cargar la llave privada en Auth Service", e);
        }
    }

    public String generateToken(User user) {
        long expirationTime = 1000 * 60 * 60 * 24;

        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("userId", user.getId().toString())
                .claim("role", user.getRole().name())
                .claim("email", user.getEmail()) // 👈 Dato extra
                .claim("name", user.getUsername()) // 👈 Dato extra
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }
}