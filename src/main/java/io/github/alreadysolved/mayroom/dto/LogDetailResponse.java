package io.github.alreadysolved.mayroom.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.alreadysolved.mayroom.domain.log.Log;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder // @AllArgsContructor
public class LogDetailResponse {
    private Long id;
    private String title;
    private String content;
    private LocalDate logDate;
    // LocalDateTime에 포함된 초와 나노초를 버리고 분까지만 JSON으로 변환해줌
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime createdAt;

    public static LogDetailResponse from(Log log) {
        return LogDetailResponse.builder()
                .id(log.getId())
                .title(log.getTitle())
                .content(log.getContent())
                .logDate(log.getLogDate())
                .createdAt(log.getCreatedAt())
                .build();
    }
}
