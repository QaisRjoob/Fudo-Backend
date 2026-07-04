package com.fudo.shared.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * The actual signature/issuer/audience verification + claim extraction, shared
 * between {@link JwtAuthenticationFilter} (servlet requests) and any
 * non-servlet entry point that still needs to authenticate a JWT, such as
 * MessagingAPI's WebSocket handshake.
 */
public class JwtTokenValidator {

    private final JwtProperties jwtProperties;
    private final SecretKey signingKey;

    public JwtTokenValidator(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.signingKey = Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8));
    }

    /** @throws JwtException | IllegalArgumentException if the token is missing, malformed, expired or mismatched. */
    public Claims validate(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(signingKey)
                .requireIssuer(jwtProperties.getIssuer())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        if (!claims.getAudience().contains(jwtProperties.getAudience())) {
            throw new JwtException("Token audience does not match expected audience.");
        }
        return claims;
    }

    public FudoUserPrincipal toPrincipal(Claims claims) {
        return new FudoUserPrincipal(
                UUID.fromString(claims.getSubject()),
                claims.get("email", String.class),
                claims.get("username", String.class),
                claims.get("displayName", String.class),
                Boolean.TRUE.equals(claims.get("isAdmin", Boolean.class)),
                Boolean.TRUE.equals(claims.get("isActive", Boolean.class)));
    }
}
