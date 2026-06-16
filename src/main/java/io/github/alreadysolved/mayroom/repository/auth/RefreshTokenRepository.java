package io.github.alreadysolved.mayroom.repository.auth;

import io.github.alreadysolved.mayroom.domain.auth.RefreshToken;

public interface RefreshTokenRepository {
    // upsert임
    void save(RefreshToken refreshToken);
    RefreshToken findByUserId(Long userId);
}
