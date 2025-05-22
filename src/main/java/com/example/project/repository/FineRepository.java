package com.example.project.repository;

import com.example.project.entity.Fine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.jpa.repository.TransactionControl;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.QueryHint;
import java.util.Date;
import java.util.List;

@Repository
public interface FineRepository extends JpaRepository<Fine, Long> {

    @Query("SELECT f FROM Fine f WHERE f.fineId = :fineId AND f.status = :status")
    Fine findByFineIdAndStatus(Long fineId, String status);

    @Transactional
    @Modifying
    @Query("UPDATE Fine f SET f.status = :status, f.paymentDate = :paymentDate WHERE f.fineId = :fineId")
    void updateFineStatusAndPaymentDate(Long fineId, String status, Date paymentDate);

    @Transactional
    @Modifying
    void save(Fine fine);

    @Transactional
    @Modifying
    void saveFineRecords(List<Fine> fineRecords);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO fines (patronId, loanId, fineAmount, issueDate, dueDate, status) VALUES (:patronId, :loanId, :fineAmount, CURRENT_DATE, CURRENT_DATE + 30, 'PENDING')", nativeQuery = true)
    void insertFine(Long patronId, Long loanId, BigDecimal fineAmount);
}