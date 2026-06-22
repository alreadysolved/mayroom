package io.github.alreadysolved.mayroom.controller;

import io.github.alreadysolved.mayroom.domain.CustomUserDetails;
import io.github.alreadysolved.mayroom.domain.user.User;
import io.github.alreadysolved.mayroom.dto.*;
import io.github.alreadysolved.mayroom.service.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api")
@RequiredArgsConstructor
public class LogController {

    private final LogService logService;

    // 일지 목록 반환
    @GetMapping("/user/logs")
    public ResponseEntity<LogPageResponse> getLogPage(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page) {

        LogPageResponse logPageResponse = logService.getLogPage(customUserDetails.getId(), keyword, page);

        return ResponseEntity.ok(logPageResponse);
    }

    // 일지 업로드
    @PostMapping("/user/logs")
    public ResponseEntity<LogCreateResponse> createLog(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody LogCreateRequest logCreateRequest) {
        LogCreateResponse logCreateResponse = logService.createLog(customUserDetails.getId(), logCreateRequest);

        return ResponseEntity.ok(logCreateResponse);
    }

    // 일지 상세
    @GetMapping("/user/logs/{id}")
    public ResponseEntity<LogDetailResponse> getLogDetail(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long id) {
        // user를 통째로 보내지 않고 user의 id만 전달
        LogDetailResponse logDetailResponse = logService.getLogDetail(customUserDetails.getId(), id);

        return ResponseEntity.ok(logDetailResponse);
    }

    // 일지 삭제
    @DeleteMapping("/user/log/{id}")
    public ResponseEntity<Void> deleteLog(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long id) {
        logService.deleteLog(customUserDetails.getId(), id);

        return ResponseEntity.ok().build();
    }

    // 일지 수정
    @PutMapping("/user/log/{id}")
    public ResponseEntity<LogUpdateResponse> updateLog(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long id,
            @RequestBody LogUpdateRequest logUpdateRequest) {
        LogUpdateResponse logUpdateResponse = logService.updateLog(customUserDetails.getId(), id, logUpdateRequest);

        return ResponseEntity.ok(logUpdateResponse);
    }
}
