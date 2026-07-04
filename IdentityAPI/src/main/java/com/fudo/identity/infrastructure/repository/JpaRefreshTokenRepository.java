package com.fudo.identity.infrastructure.repository;

import com.fudo.identity.entity.RefreshToken;
import com.fudo.identity.interfaces.RefreshTokenRepository;
import com.fudo.identity.repository.RefreshTokenJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
public class JpaRefreshTokenRepository implements RefreshTokenRepository {

    private final RefreshTokenJpaRepository jpaRepository;

    public JpaRefreshTokenRepository(RefreshTokenJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<RefreshToken> getByToken(String token) {
        return jpaRepository.findByToken(token);
    }

    @Override
    public RefreshToken add(RefreshToken token) {
        return jpaRepository.save(token);
    }

    @Override
    @Transactional
    public void revoke(String token) {
        jpaRepository.revokeByToken(token);
    }

    @Override
    @Transactional
    public void revokeAllForUser(UUID userId) {
        jpaRepository.revokeAllForUser(userId);
    }
}
