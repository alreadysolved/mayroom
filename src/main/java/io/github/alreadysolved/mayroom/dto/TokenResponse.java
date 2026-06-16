package io.github.alreadysolved.mayroom.dto;

import io.github.alreadysolved.mayroom.config.jwt.TokenDto;
import lombok.Getter;

@Getter
public class TokenResponse {
    private String accessToken;
    private String refreshToken;

    public TokenResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public static TokenResponse from(TokenDto tokenDto) {
        return new TokenResponse(tokenDto.getAccessToken(), tokenDto.getRefreshToken());
    }
}
