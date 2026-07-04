package com.fudo.shared.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/** Equivalent of reading {@code User.FindFirstValue(ClaimTypes.NameIdentifier)} in a controller. */
public final class CurrentUser {

    private CurrentUser() {
    }

    public static Optional<FudoUserPrincipal> get() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof FudoUserPrincipal principal)) {
            return Optional.empty();
        }
        return Optional.of(principal);
    }

    public static FudoUserPrincipal require() {
        return get().orElseThrow(() -> new com.fudo.shared.exception.UnauthorizedException("Authentication required."));
    }
}
