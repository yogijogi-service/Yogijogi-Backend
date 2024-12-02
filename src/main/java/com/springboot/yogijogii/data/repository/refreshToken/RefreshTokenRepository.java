package com.springboot.yogijogii.data.repository.refreshToken;

import com.springboot.yogijogii.refreshToken.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository {
    void save(RefreshToken refreshToken);
    Optional<RefreshToken> findByUsername(String email);
    void delete(String username);
}
