package com.example.project.service.impl;

import com.example.project.entity.AuditLog;
import com.example.project.repository.AuditLogRepository;
import com.example.project.repository.MonthlyStatsRepository;
import com.example.project.service.MonthlyStatsService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Map;

@Service
public class MonthlyStatsServiceImpl implements MonthlyStatsService {

    private final MonthlyStatsRepository monthlyStatsRepository;
    private final AuditLogRepository auditLogRepository;
    private final JdbcTemplate jdbcTemplate;

    public MonthlyStatsServiceImpl(MonthlyStatsRepository monthlyStatsRepository, AuditLogRepository auditLogRepository, JdbcTemplate jdbcTemplate) {
        this.monthlyStatsRepository = monthlyStatsRepository;
        this.auditLogRepository = auditLogRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    @Cacheable("monthlyStats")
    public void generateMonthlyStats(Integer year, Integer month) {
        if (year == null || month == null || month < 1 || month > 12) {
            throw new IllegalArgumentException("Invalid year or month");
        }

        // Set date range for the month
        Date startDate = Date.valueOf(LocalDate.of(year, month, 1));
        Date endDate = Date.valueOf(LocalDate.of(year, month, 1).plusMonths(1).minusDays(1));

        // Create temporary table to combine statistics
        String query = "CREATE TEMP TABLE monthly_stats AS "
                + "WITH loan_stats AS (SELECT COUNT(*) as total_loans, COUNT(CASE WHEN status = 'OVERDUE' THEN 1 END) as overdue_loans, COUNT(DISTINCT patron_id) as active_borrowers FROM loans WHERE loan_date BETWEEN ? AND ?), "
                + "fine_stats AS (SELECT SUM(amount) as total_fines, COUNT(CASE WHEN status = 'PAID' THEN 1 END) as paid_fines, COUNT(CASE WHEN status = 'PENDING' THEN 1 END) as pending_fines FROM fines WHERE issue_date BETWEEN ? AND ?), "
                + "book_stats AS (SELECT COUNT(DISTINCT b.book_id) as books_in_circulation, SUM(b.available_copies) as total_available_copies, AVG(COALESCE(r.rating, 0)) as average_rating FROM books b LEFT JOIN book_reviews r ON b.book_id = r.book_id WHERE r.review_date BETWEEN ? AND ? OR r.review_date IS NULL), "
                + "event_stats AS (SELECT COUNT(*) as total_events, SUM(current_participants) as total_participants, AVG(CAST(current_participants AS FLOAT) / NULLIF(max_participants, 0)) * 100 as avg_capacity_utilization FROM library_events WHERE event_date BETWEEN ? AND ?) "
                + "SELECT * FROM loan_stats, fine_stats, book_stats, event_stats";
        try {
            jdbcTemplate.execute(query, startDate, endDate, startDate, endDate, startDate, endDate, startDate, endDate);

            // Store results in audit log
            Map<String, Object> result = jdbcTemplate.queryForMap(query, startDate, endDate, startDate, endDate, startDate, endDate, startDate, endDate);
            AuditLog auditLog = new AuditLog("monthly_statistics", startDate.toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant().getEpochSecond(), "INSERT", new java.sql.Timestamp(System.currentTimeMillis()), result);
            auditLogRepository.saveAuditLogEntry(auditLog);

            // Drop temporary table
            jdbcTemplate.execute("DROP TABLE monthly_stats");
        } catch (Exception e) {
            throw new RuntimeException("Error generating monthly stats", e);
        }
    }
}