package com.example.project.controller;

import com.example.project.dto.MonthlyStatsRequest;
import com.example.project.service.MonthlyStatsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class MonthlyStatsController {

    private final MonthlyStatsService monthlyStatsService;

    public MonthlyStatsController(MonthlyStatsService monthlyStatsService) {
        this.monthlyStatsService = monthlyStatsService;
    }

    @PostMapping("/generate-monthly-statistics")
    public ResponseEntity<?> generateMonthlyStats(@Valid @RequestBody MonthlyStatsRequest request) {
        try {
            int year = request.getYear();
            int month = request.getMonth();
            Object result = monthlyStatsService.generateMonthlyStats(year, month);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (javax.validation.ConstraintViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Validation error: " + e.getMessage());
        } catch (com.example.project.service.BusinessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Business error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error generating monthly statistics");
        }
    }
}