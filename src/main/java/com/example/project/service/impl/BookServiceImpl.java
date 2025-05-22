package com.example.project.service.impl;

import com.example.project.entity.Book;
import com.example.project.entity.Loan;
import com.example.project.entity.Patron;
import com.example.project.repository.BookRepository;
import com.example.project.repository.LoanRepository;
import com.example.project.repository.PatronRepository;
import com.example.project.service.BookService;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final PatronRepository patronRepository;
    private final LoanRepository loanRepository;
    private final BookService bookService;

    public BookServiceImpl(BookRepository bookRepository, PatronRepository patronRepository, LoanRepository loanRepository, BookService bookService) {
        this.bookRepository = bookRepository;
        this.patronRepository = patronRepository;
        this.loanRepository = loanRepository;
        this.bookService = bookService;
    }

    @Override
    public void processBookLoan(Long patronId, Long bookId, Integer loanDays) {
        if (patronId == null || bookId == null) {
            throw new RuntimeException("Patron ID and Book ID are required");
        }

        if (loanDays == null || loanDays <= 0) {
            throw new RuntimeException("Loan days must be a positive integer");
        }

        Integer availableCopies = bookRepository.countAvailableCopies(bookId);
        Patron patron = patronRepository.findById(patronId).orElseThrow(() -> new RuntimeException("Patron not found"));
        Integer activeLoans = patronRepository.countActiveLoans(patronId);

        if (availableCopies <= 0) {
            throw new RuntimeException("Book is not available for loan");
        }

        if (!patron.getStatus().equals("ACTIVE")) {
            throw new RuntimeException("Patron account is not active");
        }

        if (activeLoans >= 5) {
            throw new RuntimeException("Patron has reached maximum number of loans");
        }

        Loan loan = new Loan(patronId, bookId, loanDays);
        loanRepository.save(loan);

        bookService.updateBookAvailability(bookId, -1);
    }
}