package io.github.alreadysolved.mayroom.exception;

public class LogNotFoundException extends BusinessException {
    public LogNotFoundException(String message) {
        super(ErrorCode.LOG_NOT_FOUND, message);
    }

    public LogNotFoundException() {
        super(ErrorCode.LOG_NOT_FOUND);
    }
}
