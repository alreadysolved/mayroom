package io.github.alreadysolved.mayroom.repository.report;

import io.github.alreadysolved.mayroom.domain.report.Report;
import io.github.alreadysolved.mayroom.dto.ReportPageElement;

import java.util.List;

public interface ReportRepository {
    void save(Report report);
    Report findById(Long id);
    Long findUserIdByReportId(Long reportId);
    List<ReportPageElement> findPageElementsByUserId(Long userId, String keyword, int offset, int size);
    int countByUserId(Long userId, String keyword);
    void deleteById(Long id);
}
