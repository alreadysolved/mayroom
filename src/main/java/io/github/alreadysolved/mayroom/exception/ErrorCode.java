package io.github.alreadysolved.mayroom.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    // 'code(상태 코드, 기본 메시지)' 형태
    // ErrorCode.USER_NOT_FOUND.name() == "USER_NOT_FOUND"
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 유저를 찾을 수 없습니다."),
    LOG_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 일지를 찾을 수 없습니다."),
    REPORT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 보고서를 찾을 수 없습니다."),

    LOG_ACCESS_DENIED(HttpStatus.FORBIDDEN, "해당 일지에 접근할 권한이 없습니다."),
    REPORT_ACCESS_DENIED(HttpStatus.FORBIDDEN, "해당 보고서에 접근할 권한이 없습니다."),


    ACCESS_TOKEN_MISSING(HttpStatus.UNAUTHORIZED, "엑세스 토큰을 전달받지 못했습니다."),
    ACCESS_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "만료된 엑세스 토큰입니다."),
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 엑세스 토큰입니다."),

    REFRESH_TOKEN_MISSING(HttpStatus.UNAUTHORIZED, "리프레시 토큰을 전달받지 못했습니다."),
    REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "만료된 리프레시 토큰입니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 리프레시 토큰입니다.");


    private final HttpStatus status;
    private final String message;
}
