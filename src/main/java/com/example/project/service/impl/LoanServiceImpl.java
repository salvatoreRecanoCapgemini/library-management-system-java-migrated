package com.example.project.service.impl;

import com.example.project.entity.Loan;
import com.example.project.repository.LoanRepository;
import com.example.project.repository.ReservationRepository;
import com.example.project.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Service
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;
    private final ReservationRepository reservationRepository;

    @Autowired
    public LoanServiceImpl(LoanRepository loanRepository, ReservationRepository reservationRepository) {
        this.loanRepository = loanRepository;
        this.reservationRepository = reservationRepository;
    }

    @Override
    public void extendLoanPeriod(Long loanId, Integer extensionDays) {
        Objects.requireNonNull(loanId, "Loan ID is required");
        Objects.requireNonNull(extensionDays, "Extension days is required");
        if (extensionDays <= 0) {
            throw new RuntimeException("Extension days must be a positive integer");
        }

        Loan loan = loanRepository.findActiveLoanById(loanId);

        if (loan == null) {
            throw new RuntimeException("Active loan not found");
        }

        if (loan.getExtensionsCount() >= 2) {
            throw new RuntimeException("Maximum extensions reached");
        }

        boolean hasReservations = reservationRepository.hasPendingReservations(loan.getBookId());

        if (hasReservations) {
            throw new RuntimeException("Book has pending reservations");
        }

        LocalDate newDueDate = loan.getDueDate().plus(extensionDays, ChronoUnit.DAYS);
        loanRepository.updateLoanDueDateAndExtensionsCount(loanId, newDueDate, loan.getExtensionsCount() + 1);
    }
}