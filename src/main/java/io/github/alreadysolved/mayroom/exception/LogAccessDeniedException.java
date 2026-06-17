package io.github.alreadysolved.mayroom.exception;

public class LogAccessDeniedException extends BusinessException {
    public LogAccessDeniedException(String message) {
        super(ErrorCode.LOG_ACCESS_DENIED, message);
    }

    public LogAccessDeniedException() {
        super(ErrorCode.LOG_ACCESS_DENIED);
    }
}
