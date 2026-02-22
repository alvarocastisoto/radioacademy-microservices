package com.alvaro.radioacademy.auth.security;

import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource; // 👈 Ojo a esta importación
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;

import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import com.alvaro.radioacademy.auth.entity.User;
import com.alvaro.radioacademy.shared.util.RSAKeyUtils;

@Service
public class JwtProvider {

    private final PrivateKey privateKey;
    private final long jwtExpiration;

    public JwtProvider(
            // 👇 CAMBIO CLAVE: Usamos Resource en lugar de String
            @Value("${app.jwt.private-key}") Resource privateKeyResource,
            @Value("${app.jwt.expiration-time}") long jwtExpiration) {

        this.jwtExpiration = jwtExpiration;
        try {
            // 👇 Leemos el contenido real del archivo aquí mismo
            String privateKeyContent = new String(privateKeyResource.getInputStream().readAllBytes(),
                    StandardCharsets.UTF_8);

            // Y ahora sí, le pasamos el bloque de texto gigante a tu utilidad
            this.privateKey = RSAKeyUtils.getPrivateKey(privateKeyContent);

        } catch (Exception e) {
            throw new RuntimeException("Error al inicializar la llave RSA", e);
        }
    }

    public String createToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole().name());
        claims.put("userId", user.getId());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }
}