package io.github.alreadysolved.mayroom.service;

import io.github.alreadysolved.mayroom.domain.CustomUserDetails;
import io.github.alreadysolved.mayroom.domain.user.User;
import io.github.alreadysolved.mayroom.exception.UserNotFoundException;
import io.github.alreadysolved.mayroom.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
// 안 쓰이는 데다가 메소드 수정도 해야함
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username){
        System.out.println("🔥 [시큐리티 뷰어] loadUserByUsername이 지금 호출되었습니다! 유저: " + username);
        User user = userRepository.findByEmail(username);

        if(user == null) throw new UserNotFoundException("가입되지 않은 이메일입니다.");

        // new User(String username, @Nullable String password, @NotNull Collection<? extends GrantedAuthority> authorities)
        // new UsernamePasswordAuthenticationToken(Object principal, @Nullable Object credentials, @Nullable Collection<? extends GrantedAuthority> authorities)

        return new CustomUserDetails(user);
    }
}
