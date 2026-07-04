package com.fudo.interaction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

@SpringBootApplication(scanBasePackages = "com.fudo", exclude = UserDetailsServiceAutoConfiguration.class)
public class InteractionApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(InteractionApiApplication.class, args);
    }
}
