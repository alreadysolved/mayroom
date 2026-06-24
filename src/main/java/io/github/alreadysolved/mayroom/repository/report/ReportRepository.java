package io.github.alreadysolved.mayroom.repository.report;

import io.github.alreadysolved.mayroom.dto.ReportPageElement;

import java.util.List;

public interface ReportRepository {
    List<ReportPageElement> findPageElementsByUserId(Long userId, String keyword, int offset, int size);
}
