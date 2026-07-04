package com.fudo.messaging.websocket;

import com.fudo.shared.security.JwtTokenValidator;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

/**
 * Equivalent of the original's JwtBearerEvents.OnMessageReceived hook that
 * pulled the token off the query string for requests under /hubs - browsers
 * cannot set an Authorization header on the WebSocket upgrade request, so the
 * original (and this port) accept ?access_token=... instead, scoped to this
 * one handshake endpoint only.
 */
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    private static final Logger log = LoggerFactory.getLogger(JwtHandshakeInterceptor.class);
    static final String USER_ID_ATTRIBUTE = "fudoUserId";

    private final JwtTokenValidator jwtTokenValidator;

    public JwtHandshakeInterceptor(JwtTokenValidator jwtTokenValidator) {
        this.jwtTokenValidator = jwtTokenValidator;
    }

    @Override
    public boolean beforeHandshake(@NonNull ServerHttpRequest request, @NonNull ServerHttpResponse response,
                                    @NonNull WebSocketHandler wsHandler, @NonNull Map<String, Object> attributes) {
        String token = extractToken(request);
        if (token == null) {
            response.setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED);
            return false;
        }

        try {
            Claims claims = jwtTokenValidator.validate(token);
            attributes.put(USER_ID_ATTRIBUTE, claims.getSubject());
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            log.debug("Rejecting WebSocket handshake with invalid token: {}", ex.getMessage());
            response.setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED);
            return false;
        }
    }

    @Override
    public void afterHandshake(@NonNull ServerHttpRequest request, @NonNull ServerHttpResponse response,
                                @NonNull WebSocketHandler wsHandler, @Nullable Exception exception) {
        // no-op
    }

    private String extractToken(ServerHttpRequest request) {
        if (request instanceof ServletServerHttpRequest servletRequest) {
            return servletRequest.getServletRequest().getParameter("access_token");
        }
        return null;
    }
}
