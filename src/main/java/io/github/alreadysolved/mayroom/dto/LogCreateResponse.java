package io.github.alreadysolved.mayroom.dto;

import lombok.Getter;

@Getter
public class LogCreateResponse {
    private final Long id;

    public LogCreateResponse(Long id) {
        this.id = id;
    }
}
