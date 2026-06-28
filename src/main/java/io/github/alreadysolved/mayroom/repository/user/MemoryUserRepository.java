package io.github.alreadysolved.mayroom.repository.user;

import io.github.alreadysolved.mayroom.domain.user.User;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class MemoryUserRepository implements UserRepository {

    private final Map<Long, User> users = new ConcurrentHashMap<>();
    private AtomicLong sequence = new AtomicLong(1);

    @Override
    public void save(User user) {
        // user의 id 채워주기. 그런데 if가 꼭 필요할까?
        if (user.getId() == null) {
            user.setId(sequence.getAndAdd(1));
        }

        // 저장
        users.put(user.getId(), user);
    }

    @Override
    public User findById(Long id) {
        return users.get(id);
    }

    @Override
    public User findByEmail(String email) {
        return users.values().stream()
                .filter(user -> email.equals(user.getEmail()))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void updateLastLoginAt(Long id) {
        users.get(id).setLastLoginAt(LocalDateTime.now());
    }

    @Override
    // user에는 email, role, nickname만 채워져있는 상태
    public void updateExtraInfo(User user) {
        users.put(user.getId(), user); // 이거 사실 필요없는 줄
    }
}