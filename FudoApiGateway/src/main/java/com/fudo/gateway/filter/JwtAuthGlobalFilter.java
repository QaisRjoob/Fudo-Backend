package com.fudo.gateway.filter;

import com.fudo.gateway.config.GatewayProperties;
import com.fudo.shared.security.JwtTokenValidator;
import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Equivalent of the gateway-level app.UseAuthentication()/UseAuthorization()
 * + per-route "AuthorizationPolicy": "default" in the original YARP config.
 * Every route except the ones under fudo.gateway.public-path-prefixes (just
 * /api/auth/** in the original) must carry a valid bearer JWT to be
 * forwarded; downstream services still validate the token again themselves
 * (defense in depth, matching the original where every backend service ran
 * its own JwtBearer middleware regardless of the gateway).
 */
@Component
public class JwtAuthGlobalFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthGlobalFilter.class);

    private final JwtTokenValidator jwtTokenValidator;
    private final GatewayProperties gatewayProperties;

    public JwtAuthGlobalFilter(JwtTokenValidator jwtTokenValidator, GatewayProperties gatewayProperties) {
        this.jwtTokenValidator = jwtTokenValidator;
        this.gatewayProperties = gatewayProperties;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        if (isPublic(path)) {
            return chain.filter(exchange);
        }

        String token = resolveToken(exchange.getRequest());
        if (token == null) {
            return reject(exchange);
        }

        try {
            jwtTokenValidator.validate(token);
        } catch (JwtException | IllegalArgumentException ex) {
            log.debug("Rejecting request to {} with invalid token: {}", path, ex.getMessage());
            return reject(exchange);
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -100;
    }

    private boolean isPublic(String path) {
        return gatewayProperties.getPublicPathPrefixes().stream().anyMatch(path::startsWith);
    }

    private String resolveToken(ServerHttpRequest request) {
        String header = request.getHeaders().getFirst("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        // WebSocket upgrade requests (MessagingAPI) can't set an Authorization header,
        // so they carry the token as a query param instead, same convention as the
        // handshake itself - the gateway just needs to let it through to MessagingAPI,
        // which performs the real validation during the STOMP handshake.
        return request.getQueryParams().getFirst("access_token");
    }

    private Mono<Void> reject(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }
}
