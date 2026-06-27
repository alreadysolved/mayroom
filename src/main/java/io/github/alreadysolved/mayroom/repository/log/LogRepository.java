package io.github.alreadysolved.mayroom.repository.log;

import io.github.alreadysolved.mayroom.domain.log.Log;
import io.github.alreadysolved.mayroom.dto.LogPageElement;

import java.util.List;

public interface LogRepository {
    Long save(Log log);
    void update(Log log);
    void deleteById(Long id); // 이미 LogService에서 log의 존재 여부를 검증하기 때문에 삭제 여부를 확인할 필요가 없어 void임
    Log findById(Long id);
    List<Log> findAllByIds(List<Long> ids); // logDate순으로 정렬된 일지 목록 반환
    Long findUserIdByLogId(Long logId); // userId와 헷갈리지 않게 logId라고 써줌
    List<LogPageElement> findPageElementsByUserId(Long userId, String keyword, int offset, int size);
    int countByUserId(Long userId, String keyword);
}
