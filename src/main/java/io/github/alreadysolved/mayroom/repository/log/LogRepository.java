package io.github.alreadysolved.mayroom.repository.log;

import io.github.alreadysolved.mayroom.domain.log.Log;
import io.github.alreadysolved.mayroom.dto.LogPageElement;

import java.util.List;

public interface LogRepository {
    Long save(Log log);
    int deleteById(Long id);
    Log findById(Long id);
    Long findUserIdByLogId(Long logId); // userId와 헷갈리지 않게 logId라고 써줌
    List<LogPageElement> findPageElementsByUserId(Long userId, String keyword, int offset, int size);
    int countByUserId(Long userId, String keyword);
}
