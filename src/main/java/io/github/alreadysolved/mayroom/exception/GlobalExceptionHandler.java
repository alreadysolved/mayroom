package io.github.alreadysolved.mayroom.exception;

import com.google.genai.errors.ServerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getErrorCode().name(), e.getMessage());

        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(errorResponse);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleServerException(RuntimeException e) {
        // 구글 서버 에러 발생
        if (e.getCause() instanceof ServerException) {
            ErrorResponse errorResponse = new ErrorResponse("AI_SERVER_UNAVAILABLE", "현재 ai 서버가 원활하지 않습니다.");

            return ResponseEntity
                    .status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(errorResponse);
        }

        // 구글 에러가 아닌 일반적인 RuntimeException
        ErrorResponse errorResponse = new ErrorResponse("INTERNAL_SERVER_ERROR", "서버 내부 오류가 발생했습니다.");

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }

}
