package com.fudo.shared.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Bound from the {@code jwt.*} keys every service shares — same secret, issuer
 * and audience the .NET services used, so every Spring Boot service trusts
 * tokens IdentityAPI issues.
 */
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    private String secretKey;
    private String issuer;
    private String audience;
    private long accessTokenExpiryMinutes = 15;
    private long refreshTokenExpiryDays = 30;

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getAudience() {
        return audience;
    }

    public void setAudience(String audience) {
        this.audience = audience;
    }

    public long getAccessTokenExpiryMinutes() {
        return accessTokenExpiryMinutes;
    }

    public void setAccessTokenExpiryMinutes(long accessTokenExpiryMinutes) {
        this.accessTokenExpiryMinutes = accessTokenExpiryMinutes;
    }

    public long getRefreshTokenExpiryDays() {
        return refreshTokenExpiryDays;
    }

    public void setRefreshTokenExpiryDays(long refreshTokenExpiryDays) {
        this.refreshTokenExpiryDays = refreshTokenExpiryDays;
    }
}
