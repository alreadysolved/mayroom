package io.github.alreadysolved.mayroom.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ReportPageResponse {
    private List<ReportPageElement> reports;

    // 프론트에서 어떻게 처리?
    private int currentPage;
    private int totalPages;
    private long totalElements;
}
