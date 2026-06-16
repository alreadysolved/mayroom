package io.github.alreadysolved.mayroom.exception;

public class RefreshTokenMismatchException extends RuntimeException {
    public RefreshTokenMismatchException(String message) {
        super(message);
    }
}
