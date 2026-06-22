package io.github.alreadysolved.mayroom.dto;

import lombok.Getter;

@Getter
public class LogUpdateResponse {
    private Long id;

    public LogUpdateResponse(Long id) {
        this.id = id;
    }
}
