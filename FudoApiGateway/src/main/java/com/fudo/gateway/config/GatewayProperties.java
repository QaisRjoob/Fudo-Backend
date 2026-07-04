package com.fudo.gateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "fudo.gateway")
public class GatewayProperties {

    private List<String> publicPathPrefixes = List.of();
    private RateLimit rateLimit = new RateLimit();

    public List<String> getPublicPathPrefixes() {
        return publicPathPrefixes;
    }

    public void setPublicPathPrefixes(List<String> publicPathPrefixes) {
        this.publicPathPrefixes = publicPathPrefixes;
    }

    public RateLimit getRateLimit() {
        return rateLimit;
    }

    public void setRateLimit(RateLimit rateLimit) {
        this.rateLimit = rateLimit;
    }

    public static class RateLimit {
        private int permitsPerWindow = 100;
        private int windowSeconds = 60;

        public int getPermitsPerWindow() {
            return permitsPerWindow;
        }

        public void setPermitsPerWindow(int permitsPerWindow) {
            this.permitsPerWindow = permitsPerWindow;
        }

        public int getWindowSeconds() {
            return windowSeconds;
        }

        public void setWindowSeconds(int windowSeconds) {
            this.windowSeconds = windowSeconds;
        }
    }
}
