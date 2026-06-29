package io.github.alreadysolved.mayroom.repository.report;

import io.github.alreadysolved.mayroom.domain.report.Report;
import io.github.alreadysolved.mayroom.dto.ReportPageElement;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

//@Repository
@RequiredArgsConstructor
public class MyBatisReportRepository implements ReportRepository{
    private final ReportMapper reportMapper;

    @Override
    public void save(Report report) {
        reportMapper.save(report);
    }

    @Override
    public Report findById(Long id) {
        return reportMapper.findById(id);
    }

    @Override
    public Long findUserIdByReportId(Long reportId) {
        return reportMapper.findUserIdByReportId(reportId);
    }

    @Override
    public List<ReportPageElement> findPageElementsByUserId(Long userId, String keyword, int offset, int size) {
        return reportMapper.findPageElementsByUserId(userId, keyword, offset, size);
    }
}
