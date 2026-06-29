package io.github.alreadysolved.mayroom.service;

import io.github.alreadysolved.mayroom.domain.log.Log;
import io.github.alreadysolved.mayroom.domain.report.Report;
import io.github.alreadysolved.mayroom.domain.report.ReportType;
import io.github.alreadysolved.mayroom.dto.*;
import io.github.alreadysolved.mayroom.exception.LogAccessDeniedException;
import io.github.alreadysolved.mayroom.exception.ReportAccessDeniedException;
import io.github.alreadysolved.mayroom.exception.ReportNotFoundException;
import io.github.alreadysolved.mayroom.repository.log.LogRepository;
import io.github.alreadysolved.mayroom.repository.report.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.google.genai.GoogleGenAiChatModel;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final LogRepository logRepository;
    private final GoogleGenAiChatModel chatModel;


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

    public ReportDetailResponse getReportDetail(Long currentUserId, Long reportId) {
        Report report = reportRepository.findById(reportId);

        // 존재하는 보고서인지 확인
        if (report == null) throw new ReportNotFoundException();

        // 해당 보고서의 작성자가 맞는지 확인
        if (!report.getUserId().equals(currentUserId)) throw new ReportAccessDeniedException();

        // dto로 변환
        return ReportDetailResponse.from(report);
    }

    public ReportGenerateResponse generateReport(Long currentUserId, ReportGenerateRequest reportGenerateRequest) {
        List<Long> logIds = reportGenerateRequest.getLogIds();
        List<Log> logs = logRepository.findAllByIds(logIds);
        ReportType reportType = reportGenerateRequest.getReportType();

        StringBuilder promptBuilder = new StringBuilder();

        if(reportType == ReportType.SUMMARY) {
            promptBuilder.append("당신은 사용자의 개발 일지를 분석하고 요약해주는 비서입니다.\n");
            promptBuilder.append("아래 제공되는 일지 목록을 읽고, 전체적인 흐름을 파악하여 핵심 내용을 요약해주세요.\n");
        } else if (reportType == ReportType.RESUME_DRAFT) {
            promptBuilder.append("사용자의 개발 일지를 분석하여 자소서 초안을 작성해주세요.\n");
            promptBuilder.append("아래 제공되는 일지 목록을 읽고, 전체적인 흐름을 파악하여 성장 가능성을 바탕으로 핵심 내용을 요약해주세요.\n");
        }

        promptBuilder.append("--- [일지 목록 시작] ---\n");
        for (Log log : logs) {
            // 현재 접속중인 유저의 일지가 아닌 게 섞여있을 경우
            if (!log.getUserId().equals(currentUserId)) {
                throw new LogAccessDeniedException();
            }
            promptBuilder.append(String.format("[%s]\n%s\n\n: ", log.getLogDate().toString(), log.getContent()));
        }
        promptBuilder.append("--- [일지 목록 끝] ---\n");

        String content = chatModel.call(promptBuilder.toString());

        return new ReportGenerateResponse(content);
    }

    public void saveReport(Long currentUserId, ReportSaveRequest reportSaveRequest) {
        Report report = Report.builder()
                .userId(currentUserId)
                .type(reportSaveRequest.getReportType())
                .title(reportSaveRequest.getTitle())
                .content(reportSaveRequest.getContent())
                .createdAt(LocalDateTime.now())
                .build();

        reportRepository.save(report);
    }

}
