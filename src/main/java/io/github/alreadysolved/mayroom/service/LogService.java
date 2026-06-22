package io.github.alreadysolved.mayroom.service;

import io.github.alreadysolved.mayroom.domain.log.Log;
import io.github.alreadysolved.mayroom.domain.user.User;
import io.github.alreadysolved.mayroom.dto.*;
import io.github.alreadysolved.mayroom.exception.LogAccessDeniedException;
import io.github.alreadysolved.mayroom.exception.LogNotFoundException;
import io.github.alreadysolved.mayroom.repository.log.LogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LogService {

    private final LogRepository logRepository;

    public LogDetailResponse getLogDetail(Long currentUserId, Long logId) {
        Log log = logRepository.findById(logId);
        if (log == null) throw new LogNotFoundException(); // "존재하지 않거나 삭제된 일지입니다."

        // 해당 일지의 작성자인지 확인
        if (!log.getUserId().equals(currentUserId)) throw new LogAccessDeniedException();

        // dto로 변환
        return LogDetailResponse.from(log);
    }

    public LogPageResponse getLogPage(Long currentUserId, String keyword, int page){
        int offset = (page - 1) * 10; // size = 10

        List<LogPageElement> logPageElements = logRepository.findPageElementsByUserId(currentUserId, keyword, offset, 10);
        int totalElements = logRepository.countByUserId(currentUserId, keyword);
        int totalPages = (int) Math.ceil((double)totalElements / 10);

        return LogPageResponse.builder()
                .logs(logPageElements) // logPageElements -> logs로 바꿀까 ..
                .currentPage(page)
                .totalPages(totalPages)
                .totalElements(totalElements)
                .build();
    }

    public LogCreateResponse createLog(Long currentUserId, LogCreateRequest logCreateRequest) {
        Log log = Log.builder()
                .id(null)
                .userId(currentUserId)
                .title(logCreateRequest.getTitle())
                .content(logCreateRequest.getContent())
                .logDate(logCreateRequest.getLogDate())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Long id = logRepository.save(log);

        return new LogCreateResponse(id);
    }

    public void deleteLog(Long currentUserId, Long logId) {
        Long writerId = logRepository.findUserIdByLogId(logId); // log가 null이면 null 반환
        if (writerId == null) throw new LogNotFoundException(); // 존재하지 않는 일지

        if (!writerId.equals(currentUserId)) throw new LogAccessDeniedException(); // 현재 로그인된 유저가 일지 작성자가 아닐 경우 / "삭제할 권한이 없습니다"

        logRepository.deleteById(logId);
    }

    public LogUpdateResponse updateLog(Long currentUserId, Long logId, LogUpdateRequest logUpdateRequest) {
        Log log = logRepository.findById(logId);
        if (log == null) throw new LogNotFoundException(); // 존재하지 않는 일지

        if (!log.getUserId().equals(currentUserId)) throw new LogAccessDeniedException(); // 수정자가 작성자가 아님

        log.setTitle(logUpdateRequest.getTitle());
        log.setContent(logUpdateRequest.getContent());
        log.setLogDate(logUpdateRequest.getLogDate());
        log.setUpdatedAt(LocalDateTime.now());

        logRepository.update(log);

        return new LogUpdateResponse(logId);
    }
}
