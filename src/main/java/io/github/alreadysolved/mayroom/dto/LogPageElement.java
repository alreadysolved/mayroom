package io.github.alreadysolved.mayroom.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
public class LogPageElement {
    private Long id;
    private String title;
    private LocalDate logDate; // 기록 날짜(작성자가 선택 가능)
}
