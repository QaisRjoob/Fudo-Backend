package com.fudo.messaging.websocket;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.lang.NonNull;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

/** Converts the userId stashed by {@link JwtHandshakeInterceptor} into the session's Principal. */
public class PrincipalHandshakeHandler extends DefaultHandshakeHandler {

    @Override
    protected Principal determineUser(@NonNull ServerHttpRequest request, @NonNull WebSocketHandler wsHandler,
                                       @NonNull Map<String, Object> attributes) {
        Object userId = attributes.get(JwtHandshakeInterceptor.USER_ID_ATTRIBUTE);
        return userId != null ? new StompPrincipal(userId.toString()) : super.determineUser(request, wsHandler, attributes);
    }
}
