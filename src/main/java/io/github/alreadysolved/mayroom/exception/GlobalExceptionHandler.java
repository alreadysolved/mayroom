package io.github.alreadysolved.mayroom.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> entityNotFoundExceptionHandler(EntityNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(""))
    }

    // /refresh로 들어온 요청 속에 리프레시 토큰이 없음
    @ExceptionHandler(MissingRefreshTokenException.class)
    public ResponseEntity<ErrorResponse> missingRefreshTokenExceptionHandler(MissingRefreshTokenException e) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse("TOKEN_MISSING", e.getMessage()));
    }

    // 요청으로 들어온 리프레시 토큰과, DB에 있는 사용자의 리프레시 토큰 정보가 일치하지 않음
    @ExceptionHandler(RefreshTokenMismatchException.class)
    public ResponseEntity<ErrorResponse> refreshTokenMismatchExceptionHandler(RefreshTokenMismatchException e) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse("INVALID_REFRESH_TOKEN", e.getMessage()));
    }
}
