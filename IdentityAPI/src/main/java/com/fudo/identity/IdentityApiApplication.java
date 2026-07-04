package com.fudo.identity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

@SpringBootApplication(scanBasePackages = "com.fudo", exclude = UserDetailsServiceAutoConfiguration.class)
public class IdentityApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(IdentityApiApplication.class, args);
    }
}
