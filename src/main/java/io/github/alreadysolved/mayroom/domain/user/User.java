package io.github.alreadysolved.mayroom.domain.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class User {
    private Long id;

    private String email;
//    private String password;
    private String nickname;

    private Role role;

    private LocalDateTime createdAt;
    private LocalDateTime lastLoginAt;

//    private SocialProvider provider;
//    private String providerId;
}