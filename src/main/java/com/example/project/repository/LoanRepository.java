package com.example.project.repository;

import com.example.project.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.LockModeType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;
import java.util.Date;
import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

    @Query("SELECT l FROM Loan l WHERE l.id = :loanId AND l.status = 'ACTIVE'")
    Loan findActiveLoanById(Long loanId);

    @Transactional
    @Modifying
    @Query("UPDATE Loan l SET l.dueDate = :newDueDate, l.extensionsCount = :newExtensionsCount WHERE l.id = :loanId")
    void updateLoanDueDateAndExtensionsCount(Long loanId, Date newDueDate, Integer newExtensionsCount);

    @Query("SELECT l FROM Loan l WHERE l.dueDate < CURRENT_DATE")
    List<Loan> findOverdueLoans();

    @Query("SELECT l FROM Loan l JOIN FETCH l.book WHERE l.id = :loanId AND l.status = 'ACTIVE'")
    Loan findActiveLoanByIdWithBookDetails(Long loanId);

    @Lock(LockModeType.OPTIMISTIC)
    @Query("SELECT l FROM Loan l WHERE l.id = :id")
    Loan findByIdForUpdate(Long id);

    @Transactional
    @Modifying
    @Query("UPDATE Loan l SET l.dueDate = :newDueDate, l.extensionsCount = :newExtensionsCount WHERE l.id = :loanId")
    void updateLoanDueDateAndExtensionsCountWithOptimisticLocking(Long loanId, Date newDueDate, Integer newExtensionsCount);

    @Lock(LockModeType.OPTIMISTIC)
    @Query("SELECT l FROM Loan l WHERE l.id = :id")
    Loan findById(Long id);

    @Transactional
    Loan save(Loan loan);
}