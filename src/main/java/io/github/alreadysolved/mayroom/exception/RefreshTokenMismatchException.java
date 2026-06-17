package io.github.alreadysolved.mayroom.exception;

// 요청으로 들어온 리프레시 토큰과, DB에 있는 사용자의 리프레시 토큰 정보가 일치하지 않음
public class RefreshTokenMismatchException extends BusinessException {
    public RefreshTokenMismatchException(String message) {
        super(ErrorCode.INVALID_REFRESH_TOKEN, message);
    }

    public RefreshTokenMismatchException() {
        super(ErrorCode.INVALID_REFRESH_TOKEN);
    }
}
