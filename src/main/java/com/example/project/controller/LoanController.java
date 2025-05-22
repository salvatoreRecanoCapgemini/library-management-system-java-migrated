package com.example.project.controller;

import com.example.project.dto.LoanExtensionRequest;
import com.example.project.service.LoanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping("/loans/{loanId}/extend")
    public ResponseEntity<Object> extendLoanPeriod(@PathVariable Long loanId, @Valid @RequestBody LoanExtensionRequest loanExtensionRequest) {
        List<String> errorMessages = new ArrayList<>();

        if (loanId == null || loanId <= 0) {
            errorMessages.add("Invalid loan ID");
        }
        if (loanExtensionRequest.getExtensionDays() == null || loanExtensionRequest.getExtensionDays() <= 0) {
            errorMessages.add("Extension days must be a positive integer");
        }

        if (!errorMessages.isEmpty()) {
            return ResponseEntity.badRequest().body(errorMessages);
        }

        try {
            loanService.extendLoanPeriod(loanId, loanExtensionRequest.getExtensionDays());
            return ResponseEntity.ok("Loan period extended successfully");
        } catch (com.example.project.service.LoanNotFoundException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(List.of(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(List.of("An unexpected error occurred"));
        }
    }
}