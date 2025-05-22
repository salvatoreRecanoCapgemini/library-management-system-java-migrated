package com.example.project.service;

import com.example.project.entity.Loan;
import com.example.project.repository.LoanRepository;
import com.example.project.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final ReservationRepository reservationRepository;

    public LoanService(LoanRepository loanRepository, ReservationRepository reservationRepository) {
        this.loanRepository = loanRepository;
        this.reservationRepository = reservationRepository;
    }

    public void extendLoanPeriod(Long loanId, Integer extensionDays) {
        if (loanId == null) {
            throw new RuntimeException("Loan ID is required");
        }
        if (extensionDays == null || extensionDays < 0) {
            throw new RuntimeException("Extension days must be a positive integer");
        }
        if (loanRepository == null) {
            throw new RuntimeException("Loan repository is not initialized");
        }
        if (reservationRepository == null) {
            throw new RuntimeException("Reservation repository is not initialized");
        }
        Loan loan = loanRepository.findActiveLoanById(loanId);
        if (loan == null) {
            throw new RuntimeException("Active loan not found");
        }
        if (loan.getExtensionsCount() >= 2) {
            throw new RuntimeException("Maximum extensions reached");
        }
        boolean hasReservations = reservationRepository.existsByBookIdAndStatus(loan.getBookId(), "PENDING");
        if (hasReservations) {
            throw new RuntimeException("Book has pending reservations");
        }
        Date newDueDate = DateUtils.addDays(loan.getDueDate(), extensionDays);
        loanRepository.updateLoanDueDateAndExtensionsCount(loanId, newDueDate, loan.getExtensionsCount() + 1);
    }
}