package com.example.project.controller;

import com.example.project.dto.ProcessBookLoanRequest;
import com.example.project.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookLoanController {

    private final BookService bookService;

    public BookLoanController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping("/process-book-loan")
    public ResponseEntity<?> processBookLoan(@RequestBody ProcessBookLoanRequest request) {
        if (request.getPatronId() == null || request.getPatronId().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"status\":\"error\",\"message\":\"Patron ID is required\"}");
        }
        if (request.getBookId() == null || request.getBookId().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"status\":\"error\",\"message\":\"Book ID is required\"}");
        }
        if (request.getLoanDays() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"status\":\"error\",\"message\":\"Loan days are required\"}");
        }
        try {
            bookService.processBookLoan(request.getPatronId(), request.getBookId(), request.getLoanDays());
            return ResponseEntity.status(HttpStatus.OK).body("{\"status\":\"success\",\"message\":\"Book loan processed successfully\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"status\":\"error\",\"message\":\"Error processing book loan: " + e.getMessage() + "\"}");
        }
    }
}