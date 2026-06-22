package io.github.alreadysolved.mayroom.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class LogUpdateRequest {
    private String title;
    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd") // 프론트에서 보내기로 한 형식. 없애도 됨
    private LocalDate logDate;
}
