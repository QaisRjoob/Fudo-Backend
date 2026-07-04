package com.fudo.gateway;

import com.fudo.gateway.config.GatewayProperties;
import com.fudo.shared.security.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * Spring Cloud Gateway (WebFlux/Netty) replacement for the original YARP
 * reverse proxy. Deliberately does NOT pull in spring-boot-starter-security /
 * spring-security-web (the servlet stack) - JWT validation here is a small
 * hand-wired WebFilter using the shared kernel's JwtTokenValidator directly,
 * since that class has no servlet dependency and works fine on WebFlux too.
 */
@SpringBootApplication(scanBasePackages = "com.fudo.gateway")
@EnableConfigurationProperties({ JwtProperties.class, GatewayProperties.class })
public class FudoApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(FudoApiGatewayApplication.class, args);
    }
}
