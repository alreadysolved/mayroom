package io.github.alreadysolved.mayroom.exception;

public class UserNotFoundException extends BusinessException {
    public UserNotFoundException(String message) {
        super(ErrorCode.USER_NOT_FOUND, message);
    }

    public UserNotFoundException() {
        super(ErrorCode.USER_NOT_FOUND);
    }
}
