package com.fudo.identity.interfaces;

import com.fudo.identity.entity.ApplicationUser;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    Optional<ApplicationUser> getById(UUID id);

    Optional<ApplicationUser> getByEmail(String email);

    Optional<ApplicationUser> getByUsername(String username);

    boolean emailExists(String email);

    boolean usernameExists(String username);

    ApplicationUser add(ApplicationUser user);

    ApplicationUser update(ApplicationUser user);
}
