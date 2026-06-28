package io.github.alreadysolved.mayroom.domain.report;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class Report {
    private Long id;

    private Long userId;
    private ReportType type;

    private String title; // ex. 2026년 4월 월간 요약
    private String content; // AI 생성 텍스트

//    private String targetPeriod;
    private LocalDateTime createdAt;
}
