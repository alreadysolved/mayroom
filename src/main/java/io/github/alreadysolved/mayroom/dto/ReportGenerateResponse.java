package io.github.alreadysolved.mayroom.dto;

import lombok.Getter;

@Getter
public class ReportGenerateResponse {
    private String content;

    public ReportGenerateResponse(String content) {
        this.content = content;
    }
}
