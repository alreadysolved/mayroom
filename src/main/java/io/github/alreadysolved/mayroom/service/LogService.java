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

    public LogDetailResponse getLogDetail(Long logId, Long currentUserId) {
        Log log = logRepository.findById(logId);

        if (log == null) {
            throw new LogNotFoundException(); // "존재하지 않거나 삭제된 일지입니다."
        }

        // 해당 일지의 작성자인지 확인
        if (!log.getUserId().equals(currentUserId)) {
            throw new LogAccessDeniedException();
        }

        // dto로 변환
        return LogDetailResponse.from(log);
    }

    public LogPageResponse getLogPage(User user, String keyword, int page){
        int offset = (page - 1) * 10; // size = 10

        List<LogPageElement> logPageElements = logRepository.findPageElementsByUserId(user.getId(), keyword, offset, 10);
        int totalElements = logRepository.countByUserId(user.getId(), keyword);
        int totalPages = (int) Math.ceil((double)totalElements / 10);

        return LogPageResponse.builder()
                .logs(logPageElements) // logPageElements -> logs로 바꿀까 ..
                .currentPage(page)
                .totalPages(totalPages)
                .totalElements(totalElements)
                .build();
    }

    public LogCreateResponse createLog(User user, LogCreateRequest logCreateRequest) {
        Log log = Log.builder()
                .id(null)
                .userId(user.getId())
                .title(logCreateRequest.getTitle())
                .content(logCreateRequest.getContent())
                .logDate(logCreateRequest.getLogDate())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Long id = logRepository.save(log);

        return new LogCreateResponse(id);
    }
}
