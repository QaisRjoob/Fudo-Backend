package com.fudo.messaging.config;

import com.fudo.messaging.websocket.JwtHandshakeInterceptor;
import com.fudo.messaging.websocket.PrincipalHandshakeHandler;
import com.fudo.shared.security.JwtTokenValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * STOMP-over-WebSocket equivalent of the original SignalR ChatHub mapped at
 * /hubs/chat. Differences from the SignalR model, called out where they
 * matter:
 *  - SignalR's explicit JoinConversation/LeaveConversation hub methods have no
 *    equivalent here because STOMP's SUBSCRIBE/UNSUBSCRIBE to
 *    /topic/conversations/{id} already IS the join/leave - clients subscribe
 *    directly, no RPC needed.
 *  - The per-user "user_{userId}" SignalR group is replaced by Spring's
 *    built-in user-destination support (convertAndSendToUser), wired up via
 *    the JWT handshake interceptor below.
 *  - This uses Spring's in-memory SimpleBroker, matching a single-instance
 *    deployment. The original used a Redis backplane so multiple SignalR
 *    instances could share groups; for the same horizontal-scaling property
 *    here, swap enableSimpleBroker for enableStompBrokerRelay(...) pointed at
 *    a STOMP-capable broker (e.g. RabbitMQ with the rabbitmq_stomp plugin,
 *    which this solution already runs for integration events).
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final JwtTokenValidator jwtTokenValidator;

    public WebSocketConfig(JwtTokenValidator jwtTokenValidator) {
        this.jwtTokenValidator = jwtTokenValidator;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/hubs/chat")
                .setAllowedOriginPatterns("*")
                .addInterceptors(new JwtHandshakeInterceptor(jwtTokenValidator))
                .setHandshakeHandler(new PrincipalHandshakeHandler());
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue");
        registry.setApplicationDestinationPrefixes("/app");
        registry.setUserDestinationPrefix("/user");
    }
}
