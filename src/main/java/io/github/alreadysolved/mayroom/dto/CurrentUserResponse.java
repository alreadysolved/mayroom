package io.github.alreadysolved.mayroom.dto;

import io.github.alreadysolved.mayroom.domain.user.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CurrentUserResponse {
    private boolean loggedIn;
    private String nickname;
    private Role role;
}
