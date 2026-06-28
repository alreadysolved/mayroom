package io.github.alreadysolved.mayroom.controller;

import io.github.alreadysolved.mayroom.domain.CustomUserDetails;
import io.github.alreadysolved.mayroom.dto.ReportGenerateRequest;
import io.github.alreadysolved.mayroom.dto.ReportGenerateResponse;
import io.github.alreadysolved.mayroom.dto.ReportPageResponse;
import io.github.alreadysolved.mayroom.dto.ReportSaveRequest;
import io.github.alreadysolved.mayroom.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/user/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    // 보고서 목록 반환
    @GetMapping("")
    public ResponseEntity<ReportPageResponse> getReportPage(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam String keyword,
            @RequestParam int page) {
        ReportPageResponse reportPageResponse = reportService.getReportPage(customUserDetails.getId(), keyword, page);

        return ResponseEntity.ok(reportPageResponse);
    }

    // ai 보고서(요약/자소서) 생성
    @PostMapping("/generate")
    public ResponseEntity<ReportGenerateResponse> generateReport(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody ReportGenerateRequest reportGenerateRequest) {
        ReportGenerateResponse reportGenerateResponse = reportService.generateReport(customUserDetails.getId(), reportGenerateRequest);

        return ResponseEntity.ok(reportGenerateResponse);
    }

    // 생성된 ai 보고서 저장
    @PostMapping("")
    public ResponseEntity<Void> saveReport(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody ReportSaveRequest reportSaveRequest) {
        reportService.saveReport(customUserDetails.getId(), reportSaveRequest);

        return ResponseEntity.ok().build();
    }

}
