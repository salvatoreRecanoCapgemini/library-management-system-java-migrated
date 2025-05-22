package com.example.project.entity;

import com.example.project.entity.Patron;
import com.example.project.entity.Loan;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Calendar;

@Entity
@Table(name = "fines", uniqueConstraints = {@UniqueConstraint(columnNames = {"patronId", "loanId"})})
public class Fine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fine_id")
    private Long fineId;

    @NotNull
    @Column(nullable = false)
    private Long patronId;

    @NotNull
    @Column(nullable = false)
    private Long loanId;

    @NotNull(message = "Fine amount cannot be null")
    @Min(value = 0, message = "Fine amount cannot be negative")
    @Column
    private BigDecimal amount;

    @NotNull
    @Column
    private BigDecimal fineAmount;

    @NotNull(message = "Issue date cannot be null")
    @Column(nullable = false)
    private Date issueDate;

    @NotNull(message = "Due date cannot be null")
    @Column(nullable = false)
    private Date dueDate;

    @NotNull(message = "Status cannot be null")
    @Column(nullable = false)
    private String status;

    @Column
    private Date paymentDate;

    @ManyToOne
    @JoinColumn(name = "patronId", referencedColumnName = "patronId")
    private Patron patron;

    @ManyToOne
    @JoinColumn(name = "loanId", referencedColumnName = "loanId")
    private Loan loan;

    public Fine() {}

    public Fine(Long patronId, Long loanId, BigDecimal amount, BigDecimal fineAmount, Date issueDate, Date dueDate, String status) {
        this.patronId = patronId;
        this.loanId = loanId;
        this.amount = amount;
        this.fineAmount = fineAmount;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.status = status;
    }

    public Fine(Long patronId, Long loanId, BigDecimal amount, Date issueDate, Date dueDate, String status) {
        this.patronId = patronId;
        this.loanId = loanId;
        this.amount = amount;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.status = status;
        calculateFineAmount();
    }

    private void calculateFineAmount() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(issueDate);
        calendar.add(Calendar.DAY_OF_YEAR, 30);
        Date dueDate = calendar.getTime();
        long overdueDays = (dueDate.getTime() - new Date().getTime()) / (24 * 60 * 60 * 1000);
        fineAmount = BigDecimal.valueOf(overdueDays * 0.1);
    }

    public Long getFineId() {
        return fineId;
    }

    public void setFineId(Long fineId) {
        this.fineId = fineId;
    }

    public Long getPatronId() {
        return patronId;
    }

    public void setPatronId(Long patronId) {
        this.patronId = patronId;
    }

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getFineAmount() {
        return fineAmount;
    }

    public void setFineAmount(BigDecimal fineAmount) {
        this.fineAmount = fineAmount;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
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
        this.status = status;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Patron getPatron() {
        return patron;
    }

    public void setPatron(Patron patron) {
        this.patron = patron;
    }

    public Loan getLoan() {
        return loan;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }
}