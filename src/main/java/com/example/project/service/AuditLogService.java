package com.example.project.service;

import com.example.project.entity.AuditLog;
import com.example.project.repository.AuditLogRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public void logAction(String tableName, Integer recordId, String actionType, String newValues) {
        if (tableName == null || tableName.isEmpty() || recordId == null || actionType == null || actionType.isEmpty() || newValues == null || newValues.isEmpty()) {
            throw new IllegalArgumentException("Invalid input parameters");
        }
        AuditLog auditLog = new AuditLog(tableName, recordId, actionType, newValues);
        auditLogRepository.save(auditLog);
    }

    public AuditLog getAuditLogById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Invalid id");
        }
        Optional<AuditLog> auditLogOptional = auditLogRepository.findById(id);
        return auditLogOptional.orElseThrow(() -> new RuntimeException("Audit log not found"));
    }
}