package com.alvaro.radioacademy.gateway.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {
        // Rutas públicas que NO necesitan Token (registro y login)
        public static final List<String> openApiEndpoints = List.of(
                        "/api/auth/register",
                        "/api/auth/login");

        public Predicate<ServerHttpRequest> isSecured = request -> openApiEndpoints
                        .stream()
                        .noneMatch(uri -> request.getURI().getPath().contains(uri));
}