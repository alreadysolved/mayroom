package io.github.alreadysolved.mayroom.controller;

import io.github.alreadysolved.mayroom.domain.CustomUserDetails;
import io.github.alreadysolved.mayroom.dto.ReportPageResponse;
import io.github.alreadysolved.mayroom.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/user/reports")
    public ResponseEntity<ReportPageResponse> getReportPage(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam String keyword,
            @RequestParam int page) {
        ReportPageResponse reportPageResponse = reportService.getReportPage(customUserDetails.getId(), keyword, page);

        return ResponseEntity.ok(reportPageResponse);
    }
}
