package com.example.project.controller;

import com.example.project.dto.EventRegistrationRequest;
import com.example.project.entity.EventRegistration;
import com.example.project.entity.LibraryEvent;
import com.example.project.entity.Patron;
import com.example.project.repository.EventRegistrationRepository;
import com.example.project.repository.LibraryEventRepository;
import com.example.project.repository.PatronRepository;
import com.example.project.service.EventRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@RestController
public class EventRegistrationController {

    private final EventRegistrationService eventRegistrationService;
    private final EventRegistrationRepository eventRegistrationRepository;
    private final LibraryEventRepository libraryEventRepository;
    private final PatronRepository patronRepository;

    @Autowired
    public EventRegistrationController(EventRegistrationService eventRegistrationService, EventRegistrationRepository eventRegistrationRepository, LibraryEventRepository libraryEventRepository, PatronRepository patronRepository) {
        this.eventRegistrationService = eventRegistrationService;
        this.eventRegistrationRepository = eventRegistrationRepository;
        this.libraryEventRepository = libraryEventRepository;
        this.patronRepository = patronRepository;
    }

    @PostMapping("/events/{eventId}/register")
    @PreAuthorize("hasAuthority('REGISTER_FOR_EVENT')")
    public ResponseEntity<String> registerForEvent(@PathVariable Long eventId, @Valid @RequestBody EventRegistrationRequest eventRegistrationRequest) {
        if (eventId == null || eventRegistrationRequest.getPatronId() == null) {
            return ResponseEntity.badRequest().body("Event ID and Patron ID are required");
        }

        Optional<LibraryEvent> libraryEvent = libraryEventRepository.findById(eventId);
        if (!libraryEvent.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Optional<Patron> patron = patronRepository.findById(eventRegistrationRequest.getPatronId());
        if (!patron.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        try {
            eventRegistrationService.registerForEvent(eventId, eventRegistrationRequest.getPatronId());
            return ResponseEntity.ok("Registration successful");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error registering for event");
        }
    }

    @PostMapping("/register-event/{eventId}/{patronId}")
    @PreAuthorize("hasAuthority('MANAGE_EVENT_REGISTRATION')")
    public ResponseEntity<Void> manageEventRegistration(@PathVariable Long eventId, @PathVariable Long patronId) {
        if (eventId == null || patronId == null) {
            return ResponseEntity.badRequest().build();
        }

        Optional<LibraryEvent> libraryEvent = libraryEventRepository.findById(eventId);
        if (!libraryEvent.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Optional<Patron> patron = patronRepository.findById(patronId);
        if (!patron.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        try {
            eventRegistrationService.manageEventRegistration(eventId, patronId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/events/{eventId}/registrations")
    @PreAuthorize("hasAuthority('REGISTER_PATRON_FOR_EVENT')")
    public ResponseEntity<String> registerPatronForEvent(@PathVariable Long eventId, @Valid @RequestBody EventRegistrationRequest eventRegistrationRequest) {
        if (eventId == null || eventRegistrationRequest.getPatronId() == null) {
            return ResponseEntity.badRequest().body("Event ID and Patron ID are required");
        }

        Optional<LibraryEvent> libraryEvent = libraryEventRepository.findById(eventId);
        if (!libraryEvent.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Optional<Patron> patron = patronRepository.findById(eventRegistrationRequest.getPatronId());
        if (!patron.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Optional<EventRegistration> existingRegistration = eventRegistrationRepository.findByEventIdAndPatronId(eventId, eventRegistrationRequest.getPatronId());
        if (existingRegistration.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Patron is already registered for the event");
        }

        if (libraryEvent.get().getCapacity() <= eventRegistrationRepository.countByEventId(eventId)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Event is full");
        }

        try {
            eventRegistrationService.manageEventRegistration(eventId, eventRegistrationRequest.getPatronId());
            return ResponseEntity.ok("Registration successful");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error registering patron for event");
        }
    }
}