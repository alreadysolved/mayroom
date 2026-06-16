package io.github.alreadysolved.mayroom.repository.user;

import io.github.alreadysolved.mayroom.domain.user.User;

public interface UserRepository {
    Long save(User user);
    User findById(Long id);
    User findByEmail(String email);
    void updateLastLoginAt(Long id);
    void updateExtraInfo(User user);
}
