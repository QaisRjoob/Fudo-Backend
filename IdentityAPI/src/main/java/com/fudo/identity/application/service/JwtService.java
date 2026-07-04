package com.fudo.identity.application.service;

import com.fudo.identity.entity.ApplicationUser;

public interface JwtService {

    String generateAccessToken(ApplicationUser user);

    String generateRefreshToken();
}
