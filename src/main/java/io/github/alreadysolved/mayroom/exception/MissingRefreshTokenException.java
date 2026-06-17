package io.github.alreadysolved.mayroom.exception;

// /refresh로 들어온 요청 속에 리프레시 토큰이 없음
public class MissingRefreshTokenException extends BusinessException {
    public MissingRefreshTokenException(String message) {
        super(ErrorCode.REFRESH_TOKEN_MISSING, message);
    }

    public MissingRefreshTokenException() {
        super(ErrorCode.REFRESH_TOKEN_MISSING);
    }
}
