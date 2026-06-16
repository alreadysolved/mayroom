package io.github.alreadysolved.mayroom.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class LogPageResponse {
    private List<LogPageElement> logs;

    private int currentPage;
    private int totalPages;
    private long totalElements;
}
