package com.fudo.messaging.websocket;

import java.security.Principal;

/**
 * Identifies a STOMP session by user id, the equivalent of the SignalR
 * ChatHub's per-connection ClaimTypes.NameIdentifier lookup. Spring's
 * {@code convertAndSendToUser(...)} keys off {@link #getName()}, which is why
 * this wraps the userId as a string rather than the full FudoUserPrincipal.
 */
public class StompPrincipal implements Principal {

    private final String userId;

    public StompPrincipal(String userId) {
        this.userId = userId;
    }

    @Override
    public String getName() {
        return userId;
    }
}
