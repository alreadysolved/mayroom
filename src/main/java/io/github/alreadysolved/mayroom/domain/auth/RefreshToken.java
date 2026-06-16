package io.github.alreadysolved.mayroom.domain.auth;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class RefreshToken {
    private Long userId;
    private String tokenValue;
    private LocalDateTime expiredAt;
}
