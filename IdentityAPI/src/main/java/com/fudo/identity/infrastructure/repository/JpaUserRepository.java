package com.fudo.identity.infrastructure.repository;

import com.fudo.identity.entity.ApplicationUser;
import com.fudo.identity.interfaces.UserRepository;
import com.fudo.identity.repository.ApplicationUserJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class JpaUserRepository implements UserRepository {

    private final ApplicationUserJpaRepository jpaRepository;

    public JpaUserRepository(ApplicationUserJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<ApplicationUser> getById(UUID id) {
        return jpaRepository.findById(id);
    }

    @Override
    public Optional<ApplicationUser> getByEmail(String email) {
        return jpaRepository.findByEmail(email.toLowerCase());
    }

    @Override
    public Optional<ApplicationUser> getByUsername(String username) {
        return jpaRepository.findByUsername(username.toLowerCase());
    }

    @Override
    public boolean emailExists(String email) {
        return jpaRepository.existsByEmail(email.toLowerCase());
    }

    @Override
    public boolean usernameExists(String username) {
        return jpaRepository.existsByUsername(username.toLowerCase());
    }

    @Override
    public ApplicationUser add(ApplicationUser user) {
        return jpaRepository.save(user);
    }

    @Override
    public ApplicationUser update(ApplicationUser user) {
        return jpaRepository.save(user);
    }
}
