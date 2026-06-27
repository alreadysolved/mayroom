package io.github.alreadysolved.mayroom.repository.log;

import io.github.alreadysolved.mayroom.domain.log.Log;
import io.github.alreadysolved.mayroom.dto.LogPageElement;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class MemoryLogRepository implements LogRepository{

    private final Map<Long, Log> logs = new ConcurrentHashMap<>();
    private AtomicLong sequence = new AtomicLong(1);


    @Override
    public Long save(Log log) {
        log.setId(sequence.getAndAdd(1));

        logs.put(log.getId(), log);

        return log.getId();
    }

    @Override
    public void update(Log log) {
        logs.put(log.getId(), log);
    }

    @Override
    public void deleteById(Long id) {
        logs.remove(id);
//        Log removedLog = logs.remove(id);
//        return removedLog == null ? 0 : 1;
    }

    @Override
    public Log findById(Long id) {
        return logs.get(id);
    }

    @Override
    public List<Log> findAllByIds(List<Long> ids) {
        return ids.stream()
                .map(id -> logs.get(id))
                .filter(log -> Objects.nonNull(log))
                .sorted(Comparator.comparing(log -> log.getLogDate())) // logDate 기준 오름차순 정렬
                .toList();
    }

    @Override
    public Long findUserIdByLogId(Long logId) {
        Log log = logs.get(logId);
        if(log == null) {
            return null;
        }

        return log.getUserId();
    }

    @Override
    public List<LogPageElement> findPageElementsByUserId(Long userId, String keyword, int offset, int size) {
        return logs.values().stream()
                // 1. WHERE: 해당 유저의 로그만 필터링
                .filter(log -> log.getUserId().equals(userId))
                .filter(log -> {
                    if (keyword == null || keyword.isBlank()) { // 나중에 고치자
                        return true;
                    }

                    return log.getContent().contains(keyword);
                })
                // 2. ORDER BY id DESC: 최신글이 위로 오게 정렬 (ID 역순)
                // 음수가 나오면 넘김, 양수가 나오면 순서 바꿈
                .sorted((b, a) -> a.getId().compareTo(b.getId()))
                // 3. OFFSET: 앞 페이지 데이터 건너뛰기
                .skip(offset)
                // 4. LIMIT: 한 페이지에 보여줄 개수만큼 자르기
                .limit(size)
                .map(log -> LogPageElement.builder()
                        .id(log.getId())
                        .title(log.getTitle())
                        .logDate(log.getLogDate())
                        .build())
                .toList(); // 반환된 리스트는 수정 불가능
                // .collect(Collectors.toList()); 반환된 리스트 수정 가능
    }

    public int countByUserId(Long userId, String keyword) {
        return (int) logs.values().stream()
                .filter(log -> log.getUserId().equals(userId))
                .filter(log -> {
                    if (keyword == null || keyword.isBlank()) {
                        return true;
                    }

                    return log.getContent().contains(keyword);
                })
                .count();
    }
}
