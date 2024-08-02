package com.grigor.forum.services;

import com.grigor.forum.model.Log;
import com.grigor.forum.repository.LogRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class LogService {

    private final LogRepository logRepository;

    public LogService(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    public List<Log> findAll(){
        return logRepository.findAll();
    }

    public void saveLog(String message, String logger, String level) {
        Log newLog = new Log();
        newLog.setMessage(message);
        newLog.setDate(LocalDateTime.now());
        newLog.setLevel(level);
        newLog.setLogger(logger);

        logRepository.save(newLog);
    }
}