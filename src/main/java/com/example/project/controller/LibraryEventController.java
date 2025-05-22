package com.example.project.controller;

import com.example.project.dto.EventRegistrationRequest;
import com.example.project.entity.LibraryEvent;
import com.example.project.service.LibraryEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
public class LibraryEventController {

    private final LibraryEventService libraryEventService;

    @Autowired
    public LibraryEventController(LibraryEventService libraryEventService) {
        this.libraryEventService = libraryEventService;
    }

    @PostMapping("/library-events/{eventId}/cancel")
    @PostMapping("/events/{eventId}/cancel")
    public ResponseEntity<Map<String, String>> cancelEvent(@PathVariable Long eventId) {
        try {
            if (eventId == null || eventId <= 0) {
                Map<String, String> response = new HashMap<>();
                response.put("status", "error");
                response.put("message", "Invalid event ID");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            libraryEventService.cancelEvent(eventId);
            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Event cancelled successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to cancel event");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/library-events/{eventId}/reschedule")
    @PostMapping("/events/{eventId}/reschedule")
    public ResponseEntity<Map<String, String>> rescheduleEvent(@PathVariable Long eventId, @Valid @RequestBody EventRegistrationRequest request) {
        try {
            if (eventId == null || eventId <= 0) {
                Map<String, String> response = new HashMap<>();
                response.put("status", "error");
                response.put("message", "Invalid event ID");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            LocalDate newDate = request.getNewDate();
            if (newDate == null) {
                Map<String, String> response = new HashMap<>();
                response.put("status", "error");
                response.put("message", "Invalid new date");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            libraryEventService.rescheduleEvent(eventId, newDate);
            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Event rescheduled successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to reschedule event");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}