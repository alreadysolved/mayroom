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
    private AtomicLong sequence = new AtomicLong(1);


    @Override
    public void save(Report report) {
        report.setId(sequence.getAndAdd(1));

        reports.put(report.getId(), report);
    }

    @Override
    public Report findById(Long id) {
        return reports.get(id);
    }

    @Override
    public Long findUserIdByReportId(Long reportId) {
        Report report = reports.get(reportId);

        if (report == null) return null;

        return report.getUserId();
    }

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
//                        .targetPeriod(report.getTargetPeriod())
                        .createdAt(report.getCreatedAt().toLocalDate())
                        .build())
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        reports.remove(id);
    }
}
