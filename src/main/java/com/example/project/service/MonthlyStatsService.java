package com.example.project.service;

import com.example.project.entity.AuditLog;
import com.example.project.repository.AuditLogRepository;
import com.example.project.repository.MonthlyStatsRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Service
public class MonthlyStatsService {

    private final MonthlyStatsRepository monthlyStatsRepository;
    private final AuditLogRepository auditLogRepository;
    @PersistenceContext
    private final EntityManager entityManager;

    @Autowired
    public MonthlyStatsService(MonthlyStatsRepository monthlyStatsRepository, AuditLogRepository auditLogRepository, EntityManager entityManager) {
        this.monthlyStatsRepository = monthlyStatsRepository;
        this.auditLogRepository = auditLogRepository;
        this.entityManager = entityManager;
    }

    public void generateMonthlyStats(Integer year, Integer month) {
        if (year == null || month == null || month < 1 || month > 12) {
            throw new IllegalArgumentException("Invalid year or month");
        }

        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.plusMonths(1).minusDays(1);

        Date start = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        Jsonb jsonb = JsonbBuilder.create();

        String loanStats = monthlyStatsRepository.findLoanStats(year, month);
        String fineStats = monthlyStatsRepository.findFineStats(year, month);
        String bookStats = monthlyStatsRepository.findBookStats(year, month);
        String eventStats = monthlyStatsRepository.findEventStats(year, month);

        String monthlyStats = jsonb.toJson(new MonthlyStats(loanStats, fineStats, bookStats, eventStats));

        try {
            Query query = entityManager.createNativeQuery("CREATE TEMPORARY TABLE monthly_stats_temp AS SELECT * FROM monthly_stats WHERE year = :year AND month = :month");
            query.setParameter("year", year);
            query.setParameter("month", month);
            query.executeUpdate();

            query = entityManager.createNativeQuery("INSERT INTO audit_log (table_name, record_id, action_type, action_timestamp, new_values) VALUES ('monthly_stats', NULL, 'INSERT', :actionTimestamp, :newValues)");
            query.setParameter("actionTimestamp", new Timestamp(System.currentTimeMillis()));
            query.setParameter("newValues", monthlyStats);
            query.executeUpdate();

            query = entityManager.createNativeQuery("DROP TABLE monthly_stats_temp");
            query.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Error generating monthly stats", e);
        }

        AuditLog auditLog = new AuditLog("monthly_stats", null, "INSERT", new Timestamp(System.currentTimeMillis()), monthlyStats);
        auditLogRepository.saveAuditLogEntry(auditLog);
    }

    private static class MonthlyStats {
        private String loanStats;
        private String fineStats;
        private String bookStats;
        private String eventStats;

        public MonthlyStats(String loanStats, String fineStats, String bookStats, String eventStats) {
            this.loanStats = loanStats;
            this.fineStats = fineStats;
            this.bookStats = bookStats;
            this.eventStats = eventStats;
        }
    }
}