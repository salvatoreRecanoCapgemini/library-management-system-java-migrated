package com.example.project.controller.book;

import com.example.project.dto.ProcessBookLoanRequest;
import com.example.project.entity.Loan;
import com.example.project.service.BookService;
import com.example.project.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookReturnController {

    private final BookService bookService;
    private final LoanService loanService;

    @Autowired
    public BookReturnController(BookService bookService, LoanService loanService) {
        this.bookService = bookService;
        this.loanService = loanService;
    }

    @PostMapping("/book-return/{loanId}")
    public ResponseEntity<Object> processBookReturn(@PathVariable Long loanId) {
        try {
            Loan loan = loanService.getLoanById(loanId);
            if (loan == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Loan not found");
            }
            if (!loan.isActive()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Loan is not active");
            }
            bookService.processBookReturn(loanId);
            return ResponseEntity.ok("Book returned successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing book return");
        }
    }
}