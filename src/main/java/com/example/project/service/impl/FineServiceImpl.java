package com.example.project.service.impl;

import com.example.project.entity.Fine;
import com.example.project.repository.FineRepository;
import com.example.project.service.FineService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.math.BigDecimal;
import java.util.Date;

@Service
public class FineServiceImpl implements FineService {

    private final FineRepository fineRepository;

    @Autowired
    public FineServiceImpl(FineRepository fineRepository) {
        this.fineRepository = fineRepository;
    }

    @Override
    public void processFinePayment(Long fineId, BigDecimal amountPaid) {
        if (fineId == null || amountPaid == null) {
            throw new IllegalArgumentException("Fine ID and amount paid cannot be null");
        }
        if (amountPaid.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount paid must be greater than 0");
        }
        Fine fine = fineRepository.findByFineIdAndStatus(fineId, "PENDING");
        if (fine == null) {
            throw new ValidUnpaidFineNotFoundException();
        }
        if (amountPaid.compareTo(fine.getAmount()) < 0) {
            throw new PartialPaymentsNotSupportedException();
        }
        fineRepository.updateFineStatusAndPaymentDate(fineId, "PAID", new Date());
    }
}

class ValidUnpaidFineNotFoundException extends RuntimeException {
    public ValidUnpaidFineNotFoundException() {
        super("Valid unpaid fine not found");
    }
}

class PartialPaymentsNotSupportedException extends RuntimeException {
    public PartialPaymentsNotSupportedException() {
        super("Partial payments not supported");
    }
}