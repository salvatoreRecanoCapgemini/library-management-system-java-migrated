package com.example.project.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

public class ProcessBookLoanRequest {

    @NotBlank
    private String patronId;

    @NotBlank
    private String bookId;

    @NotEmpty
    private Integer loanDays;

    public ProcessBookLoanRequest(String patronId, String bookId, Integer loanDays) {
        if (patronId == null || patronId.isEmpty()) {
            throw new IllegalArgumentException("Patron ID cannot be null or empty");
        }
        if (bookId == null || bookId.isEmpty()) {
            throw new IllegalArgumentException("Book ID cannot be null or empty");
        }
        if (loanDays == null) {
            throw new IllegalArgumentException("Loan days cannot be null");
        }
        this.patronId = patronId;
        this.bookId = bookId;
        this.loanDays = loanDays;
    }

    public String getPatronId() {
        if (patronId == null || patronId.isEmpty()) {
            throw new IllegalStateException("Patron ID cannot be null or empty");
        }
        return patronId;
    }

    public void setPatronId(String patronId) {
        if (patronId == null || patronId.isEmpty()) {
            throw new IllegalArgumentException("Patron ID cannot be null or empty");
        }
        this.patronId = patronId;
    }

    public String getBookId() {
        if (bookId == null || bookId.isEmpty()) {
            throw new IllegalStateException("Book ID cannot be null or empty");
        }
        return bookId;
    }

    public void setBookId(String bookId) {
        if (bookId == null || bookId.isEmpty()) {
            throw new IllegalArgumentException("Book ID cannot be null or empty");
        }
        this.bookId = bookId;
    }

    public Integer getLoanDays() {
        if (loanDays == null) {
            throw new IllegalStateException("Loan days cannot be null");
        }
        return loanDays;
    }

    public void setLoanDays(Integer loanDays) {
        if (loanDays == null) {
            throw new IllegalArgumentException("Loan days cannot be null");
        }
        this.loanDays = loanDays;
    }
}