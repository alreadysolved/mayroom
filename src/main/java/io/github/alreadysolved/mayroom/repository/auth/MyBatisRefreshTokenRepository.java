package io.github.alreadysolved.mayroom.repository.auth;

import io.github.alreadysolved.mayroom.domain.auth.RefreshToken;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MyBatisRefreshTokenRepository implements RefreshTokenRepository{

    private final RefreshTokenMapper refreshTokenMapper;


    @Override
    public void save(RefreshToken refreshToken) {
        refreshTokenMapper.save(refreshToken);
    }

    @Override
    public RefreshToken findByUserId(Long userId) {
        return refreshTokenMapper.findByUserId(userId);
    }

    @Override
    public void deleteByUserId(Long userId) {
        refreshTokenMapper.deleteByUserId(userId);
    }


}
