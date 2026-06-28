package io.github.alreadysolved.mayroom.repository.report;

import io.github.alreadysolved.mayroom.domain.report.Report;
import io.github.alreadysolved.mayroom.dto.ReportPageElement;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReportMapper {
    void save(Report report);
    List<ReportPageElement> findPageElementsByUserId(Long userId, String keyword, int offset, int size);
}
