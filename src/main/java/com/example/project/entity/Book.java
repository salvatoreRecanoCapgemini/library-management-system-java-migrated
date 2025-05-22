package com.example.project.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "books", uniqueConstraints = @UniqueConstraint(columnNames = "bookId"))
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookId;

    @NotNull
    @Size(max = 100)
    @Column(length = 100)
    private String title;

    @NotNull
    @Column
    private Integer availableCopies;

    @OneToMany(mappedBy = "book")
    private List<Loan> loans;

    public Book() {}

    public Book(String title, Integer availableCopies) {
        this.title = title;
        this.availableCopies = availableCopies;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title != null && title.length() > 100) {
            throw new IllegalArgumentException("Title cannot exceed 100 characters");
        }
        this.title = title;
    }

    public Integer getAvailableCopies() {
        return availableCopies;
    }

    public void setAvailableCopies(Integer availableCopies) {
        if (availableCopies == null) {
            throw new NullPointerException("Available copies cannot be null");
        }
        this.availableCopies = availableCopies;
    }

    public List<Loan> getLoans() {
        return loans;
    }

    public void setLoans(List<Loan> loans) {
        this.loans = loans;
    }
}