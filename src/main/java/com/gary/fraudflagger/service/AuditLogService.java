package com.gary.fraudflagger.service;

import com.gary.fraudflagger.model.AuditLog;
import com.gary.fraudflagger.repository.AuditLogRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public void log(String username, String action, Long flagId) { //write a record
        AuditLog entry = new AuditLog(username, action, flagId);
        auditLogRepository.save(entry);
    }

    public List<AuditLog> getAll(){
        return auditLogRepository.findAllByOrderByTimestampDesc(); //fetches records for the view
    }
}
