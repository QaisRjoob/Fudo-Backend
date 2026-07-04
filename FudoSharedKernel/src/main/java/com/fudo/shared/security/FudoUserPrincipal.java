package com.fudo.shared.security;

import java.util.UUID;

/**
 * The authenticated caller, decoded from the JWT claims IdentityAPI issues.
 * Available as {@code Authentication#getPrincipal()} once
 * {@link JwtAuthenticationFilter} has run, mirroring what the .NET services
 * read off {@code ClaimTypes.*} / the custom claims on {@code User}.
 */
public record FudoUserPrincipal(
        UUID userId,
        String email,
        String username,
        String displayName,
        boolean admin,
        boolean active) {
}
