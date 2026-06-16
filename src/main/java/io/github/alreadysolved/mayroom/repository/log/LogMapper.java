package io.github.alreadysolved.mayroom.repository.log;

import io.github.alreadysolved.mayroom.domain.log.Log;
import io.github.alreadysolved.mayroom.dto.LogPageElement;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LogMapper {
    void save(Log log);
    Log findById(Long id);
    List<LogPageElement> findPageElementsByUserId(Long userId, String keyword, int offset, int size);
    int countByUserId(Long userId, String keyword);
}
