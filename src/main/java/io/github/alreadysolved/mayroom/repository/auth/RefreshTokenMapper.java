package io.github.alreadysolved.mayroom.repository.auth;

import io.github.alreadysolved.mayroom.domain.auth.RefreshToken;

public interface RefreshTokenMapper {
    void save(RefreshToken refreshToken);
    RefreshToken findByUserId(Long userId);
}
