package io.github.alreadysolved.mayroom.dto;

import io.github.alreadysolved.mayroom.domain.report.ReportType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
public class ReportPageElement {
    private Long id;
    private String title;
    private ReportType type;
//    private String targetPeriod;
    private LocalDate createdAt;
}
