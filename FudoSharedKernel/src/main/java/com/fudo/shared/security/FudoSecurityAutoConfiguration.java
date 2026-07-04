package com.fudo.shared.security;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Activates only for services that pull in spring-boot-starter-security
 * themselves. Exposes {@link JwtAuthenticationFilter} as a bean so each
 * service's own SecurityConfig can wire it into its filter chain with
 * {@code .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)}.
 */
@AutoConfiguration
@ConditionalOnClass(UsernamePasswordAuthenticationFilter.class)
@EnableConfigurationProperties(JwtProperties.class)
public class FudoSecurityAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtProperties jwtProperties) {
        return new JwtAuthenticationFilter(jwtProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    public JwtTokenValidator jwtTokenValidator(JwtProperties jwtProperties) {
        return new JwtTokenValidator(jwtProperties);
    }
}
