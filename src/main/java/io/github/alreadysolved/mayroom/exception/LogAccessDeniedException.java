package io.github.alreadysolved.mayroom.exception;

public class LogAccessDeniedException extends RuntimeException {
    public LogAccessDeniedException(String message) {
        super(message);
    }
}
