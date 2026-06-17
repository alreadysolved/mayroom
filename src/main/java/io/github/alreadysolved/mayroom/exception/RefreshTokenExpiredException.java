package io.github.alreadysolved.mayroom.exception;

public class RefreshTokenExpiredException extends BusinessException{
    public RefreshTokenExpiredException(String message) {
        super(ErrorCode.REFRESH_TOKEN_EXPIRED, message);
    }

    public RefreshTokenExpiredException() {
        super(ErrorCode.REFRESH_TOKEN_EXPIRED);
    }
}
