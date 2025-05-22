package com.example.project.service;

import com.example.project.entity.Book;
import com.example.project.entity.Fine;
import com.example.project.entity.Loan;
import com.example.project.entity.Patron;
import com.example.project.exception.BookNotAvailableException;
import com.example.project.exception.PatronAccountNotActiveException;
import com.example.project.exception.PatronHasReachedMaximumLoansException;
import com.example.project.repository.BookRepository;
import com.example.project.repository.FineRepository;
import com.example.project.repository.LoanRepository;
import com.example.project.repository.PatronRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final PatronRepository patronRepository;
    private final LoanRepository loanRepository;
    private final FineRepository fineRepository;

    @Autowired
    public BookService(BookRepository bookRepository, PatronRepository patronRepository, LoanRepository loanRepository, FineRepository fineRepository) {
        this.bookRepository = bookRepository;
        this.patronRepository = patronRepository;
        this.loanRepository = loanRepository;
        this.fineRepository = fineRepository;
    }

    public void updateBookAvailability(Long bookId, Integer change) {
        Book book = bookRepository.findById(bookId).orElseThrow();
        book.setAvailableCopies(book.getAvailableCopies() + change);
        bookRepository.save(book);
    }

    public void processBookLoan(Long patronId, Long bookId, Integer loanDays) {
        if (patronId == null || bookId == null || loanDays == null) {
            throw new RuntimeException("Invalid input parameters");
        }

        Integer availableCopies = bookRepository.countAvailableCopies(bookId);
        Patron patron = patronRepository.findById(patronId).orElseThrow();
        Integer activeLoans = patronRepository.countActiveLoans(patronId);

        if (availableCopies <= 0) {
            throw new BookNotAvailableException("Book is not available for loan");
        }

        if (!patron.getStatus().equals("ACTIVE")) {
            throw new PatronAccountNotActiveException("Patron account is not active");
        }

        if (activeLoans >= 5) {
            throw new PatronHasReachedMaximumLoansException("Patron has reached maximum number of loans");
        }

        Loan loan = new Loan(patronId, bookId, loanDays);
        loanRepository.save(loan);
        updateBookAvailability(bookId, -1);
    }

    public void processBookReturn(Long loanId) {
        if (loanId == null) {
            throw new RuntimeException("Invalid input parameter");
        }

        Loan loan = loanRepository.findActiveLoanById(loanId).orElseThrow(() -> new RuntimeException("Active loan not found"));
        int daysOverdue = calculateDaysOverdue(loan.getDueDate());
        BigDecimal fineAmount = calculateFineAmount(daysOverdue);

        if (fineAmount.compareTo(BigDecimal.ZERO) > 0) {
            fineRepository.insertFine(loan.getPatronId(), loanId, fineAmount);
        }

        loanRepository.updateLoanStatus(loanId, "RETURNED");
        loanRepository.updateReturnDate(loanId, LocalDate.now());
        updateBookAvailability(loan.getBookId(), 1);
    }

    private int calculateDaysOverdue(LocalDate dueDate) {
        return (int) ChronoUnit.DAYS.between(dueDate, LocalDate.now());
    }

    private BigDecimal calculateFineAmount(int daysOverdue) {
        // Implement fine calculation logic here
        // For example, a fine of $0.25 per day
        return BigDecimal.valueOf(daysOverdue * 0.25);
    }
}