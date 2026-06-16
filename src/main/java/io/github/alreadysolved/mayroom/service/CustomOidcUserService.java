package io.github.alreadysolved.mayroom.service;

import io.github.alreadysolved.mayroom.domain.CustomUserDetails;
import io.github.alreadysolved.mayroom.domain.user.Role;
import io.github.alreadysolved.mayroom.domain.user.User;
import io.github.alreadysolved.mayroom.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CustomOidcUserService extends OidcUserService {

    private final UserRepository userRepository;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        // 스프링이 userRequest 안의 Access Token과 UserInfo Endpoint를 꺼내서 구글 서버에 유저 정보를 요청함.
        OidcUser oidcUser = super.loadUser(userRequest);

        // 유저가 존재할 경우 업데이트(?), 아닐 경우 새로 저장
        User user = saveOrUpdate(oidcUser);

        // CustomUserDetails 반환하는 이유?
        // 얘가 나중에 Authentication에 담겨서 Access Token + Refresh Token 만드는 데 사용됨
        return new CustomUserDetails(user, oidcUser.getAttributes());
    }

    private User saveOrUpdate(OidcUser oidcUser) {
        String email = oidcUser.getAttribute("email");

        User user = userRepository.findByEmail(email);

        // 회원가입하지 않은 유저일 경우 저장
        if (user == null) {
            user = User.builder()
                    .email(email)
                    .role(Role.ROLE_GUEST)
                    .createdAt(LocalDateTime.now())
                    .build();

            // 저장 + 반환받은 id 심어줌
            user.setId(userRepository.save(user));
        }

        return user;
    }
}
