package com.example.project.repository;

import com.example.project.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public interface MonthlyStatsRepository extends JpaRepository<Loan, Long> {

    @Query("SELECT COUNT(l) as totalLoans, COUNT(CASE WHEN l.status = 'OVERDUE' THEN 1 END) as overdueLoans, COUNT(DISTINCT l.patronId) as activeBorrowers " +
            "FROM Loan l " +
            "WHERE FUNCTION('DATE_FORMAT', l.loanDate, '%Y-%m') = :yearMonth")
    Map<String, Object> findLoanStats(@Param("yearMonth") String yearMonth);

    @Query("SELECT COUNT(f) as totalFines, SUM(f.amount) as totalFineAmount " +
            "FROM Fine f " +
            "WHERE FUNCTION('DATE_FORMAT', f.fineDate, '%Y-%m') = :yearMonth")
    Map<String, Object> findFineStats(@Param("yearMonth") String yearMonth);

    @Query("SELECT COUNT(b) as totalBooks, COUNT(CASE WHEN b.status = 'AVAILABLE' THEN 1 END) as availableBooks " +
            "FROM Book b " +
            "WHERE FUNCTION('DATE_FORMAT', b.createDate, '%Y-%m') = :yearMonth")
    Map<String, Object> findBookStats(@Param("yearMonth") String yearMonth);

    @Query("SELECT COUNT(e) as totalEvents, COUNT(CASE WHEN e.status = 'UPCOMING' THEN 1 END) as upcomingEvents " +
            "FROM LibraryEvent e " +
            "WHERE FUNCTION('DATE_FORMAT', e.eventDate, '%Y-%m') = :yearMonth")
    Map<String, Object> findEventStats(@Param("yearMonth") String yearMonth);
}