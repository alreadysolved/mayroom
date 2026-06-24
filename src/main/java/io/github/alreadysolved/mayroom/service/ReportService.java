package io.github.alreadysolved.mayroom.service;

import io.github.alreadysolved.mayroom.dto.ReportPageElement;
import io.github.alreadysolved.mayroom.dto.ReportPageResponse;
import io.github.alreadysolved.mayroom.repository.report.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;


    // page = 현재 보여줄 페이지
    public ReportPageResponse getReportPage(Long currentUserId, String keyword, int page) {
        int size = 10; // 한 페이지 크기
        int offset = size * (page - 1);

        List<ReportPageElement> reportPageElements = reportRepository.findPageElementsByUserId(currentUserId, keyword, offset, size);
        int totalElements = 15; // 수정 예정
        int totalPages = (int) Math.ceil((double)totalElements / size);

        return ReportPageResponse.builder()
                .reports(reportPageElements)
                .currentPage(page)
                .totalPages(totalPages)
                .totalElements(totalElements)
                .build();
    }
}
