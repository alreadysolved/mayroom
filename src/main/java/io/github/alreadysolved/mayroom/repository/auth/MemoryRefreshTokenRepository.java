package io.github.alreadysolved.mayroom.repository.auth;

import io.github.alreadysolved.mayroom.domain.auth.RefreshToken;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class MemoryRefreshTokenRepository implements RefreshTokenRepository{

    private final Map<Long, RefreshToken> store = new ConcurrentHashMap<>();

    @Override
    public void save(RefreshToken refreshToken) {
        store.put(refreshToken.getUserId(), refreshToken);
    }

    @Override
    public RefreshToken findByUserId(Long userId) {
        return store.get(userId);
    }
}
