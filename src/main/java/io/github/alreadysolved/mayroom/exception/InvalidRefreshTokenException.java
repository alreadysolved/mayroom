package io.github.alreadysolved.mayroom.exception;

public class InvalidRefreshTokenException extends BusinessException{
    public InvalidRefreshTokenException(Throwable cause, String message) {
        super(ErrorCode.INVALID_REFRESH_TOKEN, message);
    }

    public InvalidRefreshTokenException(Throwable cause) {
        super(ErrorCode.INVALID_REFRESH_TOKEN);
    }
}
