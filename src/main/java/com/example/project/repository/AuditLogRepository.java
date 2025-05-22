package com.example.project.repository;

import com.example.project.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.QueryHint;
import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    @Transactional
    @Query("SELECT a FROM AuditLog a")
    List<AuditLog> findAll();

    @Transactional
    @Query("SELECT a FROM AuditLog a WHERE a.id = :id")
    AuditLog findById(@Param("id") Long id);

    @Transactional
    @Query("SELECT a FROM AuditLog a WHERE a.id = :id")
    AuditLog findAuditLogById(@Param("id") Long id);

    @Transactional
    @Query(value = "INSERT INTO audit_log (table_name, record_id, action_type, action_timestamp, new_values) VALUES (:tableName, :recordId, :actionType, :actionTimestamp, :newValues)", nativeQuery = true)
    void saveAuditLogEntry(@Param("tableName") String tableName, @Param("recordId") Integer recordId, @Param("actionType") String actionType, @Param("actionTimestamp") java.sql.Timestamp actionTimestamp, @Param("newValues") String newValues);

    @Transactional
    @Query("DELETE FROM AuditLog a WHERE a.id = :id")
    void delete(@Param("id") Long id);

    @Transactional
    AuditLog save(AuditLog log);
}