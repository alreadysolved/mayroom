package io.github.alreadysolved.mayroom.dto;

import io.github.alreadysolved.mayroom.domain.report.ReportType;
import lombok.Getter;

@Getter
public class ReportSaveRequest {
    private ReportType reportType;

    private String title;
    private String content;
}
