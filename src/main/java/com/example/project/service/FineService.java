package com.example.project.service;

import com.example.project.entity.Fine;
import com.example.project.repository.FineRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.math.BigDecimal;
import java.util.Date;

@Service
public class FineService {

    private final FineRepository fineRepository;

    @Autowired
    public FineService(FineRepository fineRepository) {
        this.fineRepository = fineRepository;
    }

    public void processFinePayment(Long fineId, BigDecimal amountPaid) {
        Fine fine = fineRepository.findByFineIdAndStatus(fineId, "PENDING");
        if (fine == null) {
            throw new RuntimeException("Valid unpaid fine not found");
        }
        if (!fine.getStatus().equals("PENDING")) {
            throw new RuntimeException("Fine is not pending");
        }
        if (amountPaid.compareTo(fine.getAmount()) < 0) {
            BigDecimal remainingAmount = fine.getAmount().subtract(amountPaid);
            throw new RuntimeException("Partial payments not supported");
        }
        fineRepository.updateFineStatusAndPaymentDate(fineId, "PAID", new Date());
    }
}