package com.example.project.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "loans", uniqueConstraints = @UniqueConstraint(columnNames = "id"))
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column
    private Long bookId;

    @NotNull
    @Column
    private Long patronId;

    @NotNull
    @Column
    private Date dueDate;

    @NotNull
    @Column
    private String status;

    @Column
    private Date returnDate;

    @Column
    private Integer extensionsCount;

    @NotNull
    @Column
    private Date loanDate;

    @ManyToOne
    @JoinColumn(name = "bookId", insertable = false, updatable = false)
    private Book book;

    @ManyToOne
    @JoinColumn(name = "patronId", insertable = false, updatable = false)
    private Patron patron;

    public Loan() {
    }

    public Loan(Long bookId, Long patronId, Date dueDate, String status, Date returnDate, Integer extensionsCount, Date loanDate) {
        this.bookId = bookId;
        this.patronId = patronId;
        this.dueDate = dueDate;
        this.status = status;
        this.returnDate = returnDate;
        this.extensionsCount = extensionsCount;
        this.loanDate = loanDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public Long getPatronId() {
        return patronId;
    }

    public void setPatronId(Long patronId) {
        this.patronId = patronId;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (!status.equals("ACTIVE") && !status.equals("RETURNED")) {
            throw new IllegalArgumentException("Invalid status. Status must be 'ACTIVE' or 'RETURNED'.");
        }
        this.status = status;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        if (this.status.equals("ACTIVE")) {
            throw new IllegalStateException("Cannot set return date for an active loan.");
        }
        this.returnDate = returnDate;
    }

    public Integer getExtensionsCount() {
        return extensionsCount;
    }

    public void setExtensionsCount(Integer extensionsCount) {
        this.extensionsCount = extensionsCount;
    }

    public Date getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(Date loanDate) {
        this.loanDate = loanDate;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Patron getPatron() {
        return patron;
    }

    public void setPatron(Patron patron) {
        this.patron = patron;
    }
}