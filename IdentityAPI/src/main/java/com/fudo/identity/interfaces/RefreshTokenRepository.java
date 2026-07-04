package com.fudo.identity.interfaces;

import com.fudo.identity.entity.RefreshToken;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository {

    Optional<RefreshToken> getByToken(String token);

    RefreshToken add(RefreshToken token);

    void revoke(String token);

    void revokeAllForUser(UUID userId);
}
