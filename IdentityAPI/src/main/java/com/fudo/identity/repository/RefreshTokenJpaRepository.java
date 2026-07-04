package com.fudo.identity.repository;

import com.fudo.identity.entity.RefreshToken;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenJpaRepository extends JpaRepository<RefreshToken, UUID> {

    @EntityGraph(attributePaths = "user")
    Optional<RefreshToken> findByToken(String token);

    @Modifying
    @Query("update RefreshToken r set r.isRevoked = true where r.token = :token")
    void revokeByToken(@Param("token") String token);

    @Modifying
    @Query("update RefreshToken r set r.isRevoked = true where r.userId = :userId and r.isRevoked = false")
    void revokeAllForUser(@Param("userId") UUID userId);
}
