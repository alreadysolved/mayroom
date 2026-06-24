package io.github.alreadysolved.mayroom.repository.report;

import io.github.alreadysolved.mayroom.domain.report.Report;
import io.github.alreadysolved.mayroom.dto.ReportPageElement;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class MemoryReportRepository implements ReportRepository{

    private final Map<Long, Report> reports = new ConcurrentHashMap<>();
//    private AtomicLong sequence = new AtomicLong(1);

    @Override
    public List<ReportPageElement> findPageElementsByUserId(Long userId, String keyword, int offset, int size) {
        return reports.values().stream()
                .filter(report -> report.getUserId().equals(userId))
                .filter(report -> report.getContent().contains(keyword))
                .sorted((b,a) -> a.getId().compareTo(b.getId()))
                .skip(offset)
                .limit(size)
                .map(report -> ReportPageElement.builder()
                        .id(report.getId())
                        .title(report.getTitle())
                        .type(report.getType())
                        .targetPeriod(report.getTargetPeriod())
                        .build())
                .toList();
    }
}
