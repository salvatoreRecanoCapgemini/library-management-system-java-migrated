package com.example.project.controller;

import com.example.project.dto.FinePaymentRequest;
import com.example.project.service.FineService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;

@RestController
public class FineController {

    private final FineService fineService;

    public FineController(FineService fineService) {
        this.fineService = fineService;
    }

    @PostMapping("/fine/payments")
    public ResponseEntity processFinePayment(@Valid @RequestBody FinePaymentRequest finePaymentRequest) {
        Long fineId = finePaymentRequest.getFineId();
        BigDecimal amountPaid = finePaymentRequest.getAmountPaid();

        if (fineId == null || amountPaid == null) {
            return ResponseEntity.badRequest().body("Fine ID and amount paid are required.");
        }

        if (amountPaid.compareTo(BigDecimal.ZERO) < 0) {
            return ResponseEntity.badRequest().body("Amount paid must be greater than 0.");
        }

        try {
            fineService.processFinePayment(fineId, amountPaid);
            return ResponseEntity.ok().body("Fine payment processed successfully.");
        } catch (Exception e) {
            if (e instanceof NullPointerException) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Fine not found.");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing fine payment: " + e.getMessage());
            }
        }
    }
}