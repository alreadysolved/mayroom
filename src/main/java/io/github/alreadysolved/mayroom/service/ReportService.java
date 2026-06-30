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
            promptBuilder.append("사용자의 개발 일지를 분석하고 요약해야 해.\n");
            promptBuilder.append("아래 제공되는 일지 목록을 읽고, 전체적인 흐름을 파악하여 핵심 내용을 요약해줘.\n");
            promptBuilder.append("날짜를 너무 딱딱 언급하지 말아줘. 언급이 필요하다면 'n월에는 ~~한 부분을 공부했다' 정도로만 사용해줘.\n");
        } else if (reportType == ReportType.RESUME_DRAFT) {
            promptBuilder.append("사용자의 개발 일지를 분석하여 자소서 초안을 작성해줘.\n");
            promptBuilder.append("아래 제공되는 일지 목록을 읽고, 전체적인 흐름을 파악하여 성장 가능성을 바탕으로 글을 적당히 늘려줘.\n");
            promptBuilder.append("[적절한 제목]\n");
            promptBuilder.append("자소서 내용\n\n");
            promptBuilder.append("이 형식으로만 답하고 부가 설명이나 첨언(ex. 조언) 같은 건 넣지 마.\n");
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

    public void deleteReport(Long currentUserId, Long reportId) {
        Long writerId = reportRepository.findUserIdByReportId(reportId); // 존재하지 않는 보고서면 null 반환

        // 존재하는 보고서인지 확인
        if (writerId == null) throw new ReportNotFoundException();

        // 해당 보고서의 작성자가 맞는지 확인
        if (!writerId.equals(currentUserId)) throw new ReportAccessDeniedException();

        reportRepository.deleteById(reportId);
    }
}
