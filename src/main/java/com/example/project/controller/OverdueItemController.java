package com.example.project.controller;

import com.example.project.service.OverdueItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class OverdueItemController {

    private final OverdueItemProcessor overdueItemProcessor;

    @Autowired
    public OverdueItemController(OverdueItemProcessor overdueItemProcessor) {
        this.overdueItemProcessor = overdueItemProcessor;
    }

    @PostMapping("/overdue-items/process")
    public ResponseEntity<Map<String, String>> processOverdueItems(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {

        try {
            LocalDate startLocalDate = startDate != null ? LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE) : null;
            LocalDate endLocalDate = endDate != null ? LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE) : null;

            overdueItemProcessor.processOverdueItems(startLocalDate, endLocalDate);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Overdue items processed successfully");
            response.put("status", "success");

            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (DateTimeParseException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Invalid date format. Please use ISO date format (yyyy-MM-dd).");
            response.put("status", "error");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Error processing overdue items: " + e.getMessage());
            response.put("status", "error");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}