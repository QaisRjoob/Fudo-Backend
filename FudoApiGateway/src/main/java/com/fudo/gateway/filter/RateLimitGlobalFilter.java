package com.fudo.gateway.filter;

import com.fudo.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * In-memory fixed-window limiter, the equivalent of the original's
 * PartitionedRateLimiter.GetFixedWindowLimiter (100 requests/minute,
 * partitioned by authenticated user, falling back to Host header). Being
 * in-memory it only holds for a single gateway instance - the original's
 * dev config also ran a single gateway process per environment. For a
 * multi-instance gateway deployment, swap this for Spring Cloud Gateway's
 * built-in RedisRateLimiter so the window is shared.
 */
@Component
public class RateLimitGlobalFilter implements GlobalFilter, Ordered {

    private final GatewayProperties gatewayProperties;
    private final ConcurrentHashMap<String, Window> windows = new ConcurrentHashMap<>();

    public RateLimitGlobalFilter(GatewayProperties gatewayProperties) {
        this.gatewayProperties = gatewayProperties;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String key = partitionKey(exchange.getRequest());
        int limit = gatewayProperties.getRateLimit().getPermitsPerWindow();
        long windowMillis = gatewayProperties.getRateLimit().getWindowSeconds() * 1000L;

        Window window = windows.compute(key, (k, existing) -> {
            long now = Instant.now().toEpochMilli();
            if (existing == null || now - existing.windowStartMillis >= windowMillis) {
                return new Window(now);
            }
            return existing;
        });

        if (window.count.incrementAndGet() > limit) {
            exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -90;
    }

    private String partitionKey(ServerHttpRequest request) {
        String authHeader = request.getHeaders().getFirst("Authorization");
        if (authHeader != null && !authHeader.isBlank()) {
            return authHeader;
        }
        String host = request.getHeaders().getFirst("Host");
        return host != null ? host : "unknown";
    }

    private static final class Window {
        final long windowStartMillis;
        final AtomicInteger count = new AtomicInteger(0);

        Window(long windowStartMillis) {
            this.windowStartMillis = windowStartMillis;
        }
    }
}
