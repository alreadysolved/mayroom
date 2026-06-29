package io.github.alreadysolved.mayroom.exception;

public class ReportNotFoundException extends BusinessException {
    public ReportNotFoundException() {
        super(ErrorCode.REPORT_NOT_FOUND);
    }

    public ReportNotFoundException(String message) {
        super(ErrorCode.REPORT_NOT_FOUND, message);
    }
}
