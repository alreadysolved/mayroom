package io.github.alreadysolved.mayroom.dto;

import io.github.alreadysolved.mayroom.domain.report.ReportType;
import lombok.Getter;

import java.util.List;

@Getter
public class ReportGenerateRequest {
    private List<Long> logIds;
    private ReportType reportType;
}
