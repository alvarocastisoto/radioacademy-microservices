package com.alvaro.radioacademy.gateway.filter;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouteValidator validator;

    @Autowired
    private JwtUtil jwtUtil;

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if (validator.isSecured.test(exchange.getRequest())) {
                // Comprueba si la petición trae el Header "Authorization"
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                }

                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                }

                try {
                    // Valida la firma del token
                    jwtUtil.validateToken(authHeader);
                    Claims claims = jwtUtil.getAllClaimsFromToken(authHeader);

                    // 💥 LA MAGIA OCURRE AQUÍ 💥
                    // Inyecta el ID y el Rol en cabeceras ocultas para los microservicios internos
                    exchange.getRequest().mutate()
                            .header("X-User-Id", claims.get("userId").toString())
                            .header("X-User-Role", claims.get("role").toString())
                            .header("X-User-Email", claims.get("email").toString()) // 👈 Cabecera extra
                            .header("X-User-Name", claims.get("name").toString()) // 👈 Cabecera extra
                            .build();

                } catch (Exception e) {
                    System.out.println("Token inválido: " + e.getMessage());
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                }
            }
            return chain.filter(exchange);
        });
    }

    public static class Config {
    }
}