package io.github.alreadysolved.mayroom.repository.user;

import io.github.alreadysolved.mayroom.domain.user.User;
import lombok.RequiredArgsConstructor;

//@Repository
@RequiredArgsConstructor
public class MyBatisUserRepository implements UserRepository {

    private final UserMapper userMapper;


    @Override
    public void save(User user) {
        userMapper.save(user);

        // UserMapper.xml에서 useGeneratedKeys="true" keyProperty="id"를 했기 때문에 id 필드가 채워져있음
//        return user.getId();
    }

    @Override
    public User findById(Long id) {
        return userMapper.findById(id);
    }

    @Override
    public User findByEmail(String email) {
        return userMapper.findByEmail(email);
    }

    @Override
    public void updateLastLoginAt(Long id) {
        userMapper.updateLastLoginAt(id);
    }

    @Override
    // user에는 email, role, nickname만 채워져있는 상태
    public void updateExtraInfo(User user) {
        userMapper.updateExtraInfo(user);
    }
}
