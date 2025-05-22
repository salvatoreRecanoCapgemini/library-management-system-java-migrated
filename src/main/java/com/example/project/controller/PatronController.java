package com.example.project.controller;

import com.example.project.dto.PatronCreateRequest;
import com.example.project.service.PatronService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

@RestController
public class PatronController {

    private final PatronService patronService;

    public PatronController(PatronService patronService) {
        this.patronService = patronService;
    }

    @PostMapping("/patrons")
    public ResponseEntity<Object> createPatron(@Valid @RequestBody PatronCreateRequest patronCreateRequest) {
        try {
            Long patronId = patronService.createPatron(patronCreateRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("patronId", patronId, "message", "Patron created successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Failed to create patron", "message", e.getMessage()));
        }
    }
}