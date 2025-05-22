package com.example.project.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

public class FinePaymentRequest {

    @NotNull
    private Long fineId;

    @NotNull
    @Positive
    private BigDecimal amountPaid;

    public FinePaymentRequest(Long fineId, BigDecimal amountPaid) {
        if (fineId == null) {
            throw new NullPointerException("Fine ID cannot be null");
        }
        if (amountPaid == null) {
            throw new NullPointerException("Amount paid cannot be null");
        }
        if (amountPaid.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount paid must be greater than 0");
        }
        this.fineId = fineId;
        this.amountPaid = amountPaid;
    }

    public Long getFineId() {
        return fineId;
    }

    public void setFineId(Long fineId) {
        if (fineId == null) {
            throw new NullPointerException("Fine ID cannot be null");
        }
        this.fineId = fineId;
    }

    public BigDecimal getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(BigDecimal amountPaid) {
        if (amountPaid == null) {
            throw new NullPointerException("Amount paid cannot be null");
        }
        if (amountPaid.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount paid must be greater than 0");
        }
        this.amountPaid = amountPaid;
    }
}