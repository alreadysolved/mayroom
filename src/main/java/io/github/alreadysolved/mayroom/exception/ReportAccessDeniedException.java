package io.github.alreadysolved.mayroom.exception;

public class ReportAccessDeniedException extends BusinessException{
    public ReportAccessDeniedException() {
        super(ErrorCode.REPORT_ACCESS_DENIED);
    }

    public ReportAccessDeniedException(String message) {
        super(ErrorCode.REPORT_ACCESS_DENIED, message);
    }
}
