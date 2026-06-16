package io.github.alreadysolved.mayroom.repository.log;

import io.github.alreadysolved.mayroom.domain.log.Log;
import io.github.alreadysolved.mayroom.dto.LogPageElement;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

//@Repository
@RequiredArgsConstructor
public class MyBatisLogRepository implements LogRepository{

    private final LogMapper logMapper;


    @Override
    public Long save(Log log) {
        logMapper.save(log);

        return log.getId();
    }

    @Override
    public Log findById(Long id) {
        return logMapper.findById(id);
    }


    @Override
    public List<LogPageElement> findPageElementsByUserId(Long userId, String keyword, int offset, int size) {
        return logMapper.findPageElementsByUserId(userId, keyword, offset, size);
    }

    @Override
    public int countByUserId(Long userId, String keyword) {
        return logMapper.countByUserId(userId, keyword);
    }
}
