package io.github.alreadysolved.mayroom.domain.log;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class Log {
    private Long id;

    private Long userId;

    private String title;
    private String content;
    private LocalDate logDate; // 기록 날짜(작성자가 선택 가능)

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}