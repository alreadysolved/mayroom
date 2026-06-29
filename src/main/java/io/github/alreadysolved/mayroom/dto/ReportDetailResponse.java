package io.github.alreadysolved.mayroom.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.alreadysolved.mayroom.domain.report.Report;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Getter
public class ReportDetailResponse {
    private Long id;
    private Long userId;

    private String title;
    private String content;

    // LocalDateTime에 포함된 초와 나노초를 버리고 분까지만 JSON으로 변환해줌
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime createdAt;

    public static ReportDetailResponse from(Report report) {
        return ReportDetailResponse.builder()
                .id(report.getId())
                .userId(report.getUserId())
                .title(report.getTitle())
                .content(report.getContent())
                .createdAt(report.getCreatedAt())
                .build();
    }
}
