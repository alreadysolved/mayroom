package io.github.alreadysolved.mayroom.repository.log;

import io.github.alreadysolved.mayroom.domain.log.Log;
import io.github.alreadysolved.mayroom.dto.LogPageElement;

import java.util.List;

public interface LogRepository {
    Long save(Log log);
    Log findById(Long id);
    List<LogPageElement> findPageElementsByUserId(Long userId, String keyword, int offset, int size);
    int countByUserId(Long userId, String keyword);
}
