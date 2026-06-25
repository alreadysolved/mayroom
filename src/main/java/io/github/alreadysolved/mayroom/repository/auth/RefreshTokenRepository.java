package io.github.alreadysolved.mayroom.repository.auth;

import io.github.alreadysolved.mayroom.domain.auth.RefreshToken;

public interface RefreshTokenRepository {
    // upsert임
    void save(RefreshToken refreshToken);
    RefreshToken findByUserId(Long userId);
    void deleteByUserId(Long userId); // 로그아웃의 최종 목적은 토큰 삭제이기 때문에 토큰이 원래 없었는지 확인할 필요가 없으므로 void
}
