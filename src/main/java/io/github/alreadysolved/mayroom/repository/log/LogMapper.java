package io.github.alreadysolved.mayroom.repository.log;

import io.github.alreadysolved.mayroom.domain.log.Log;
import io.github.alreadysolved.mayroom.dto.LogPageElement;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LogMapper {
    void save(Log log);
    int deleteById(Long id); // 없으면 0 반환
    Log findById(Long id); // 없으면 null 반환
    Long findUserIdByLogId(Long logId);
    List<LogPageElement> findPageElementsByUserId(Long userId, String keyword, int offset, int size);
    int countByUserId(Long userId, String keyword);
}
