package com.example.project.service;

import com.example.project.entity.EventRegistration;
import com.example.project.entity.LibraryEvent;
import com.example.project.entity.Patron;
import com.example.project.repository.EventRegistrationRepository;
import com.example.project.repository.LibraryEventRepository;
import com.example.project.repository.PatronRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class EventRegistrationService {

    private final EventRegistrationRepository eventRegistrationRepository;
    private final LibraryEventRepository libraryEventRepository;
    private final PatronRepository patronRepository;

    @Autowired
    public EventRegistrationService(EventRegistrationRepository eventRegistrationRepository, LibraryEventRepository libraryEventRepository, PatronRepository patronRepository) {
        this.eventRegistrationRepository = eventRegistrationRepository;
        this.libraryEventRepository = libraryEventRepository;
        this.patronRepository = patronRepository;
    }

    public EventRegistration registerForEvent(int eventId, int patronId) {
        if (eventId == 0 || patronId == 0) {
            throw new IllegalArgumentException("Event ID and Patron ID must not be null");
        }
        if (eventRegistrationRepository.existsByEventIdAndPatronId((long) eventId, (long) patronId)) {
            throw new IllegalArgumentException("Patron is already registered for this event");
        }
        LibraryEvent event = libraryEventRepository.findById((long) eventId).orElseThrow();
        if (event.getParticipants() >= event.getMaxParticipants()) {
            throw new IllegalArgumentException("Event is full");
        }
        Patron patron = patronRepository.findById((long) patronId).orElseThrow();
        EventRegistration registration = new EventRegistration(event, patron, LocalDateTime.now(), "REGISTERED");
        return eventRegistrationRepository.save(registration);
    }

    public EventRegistration cancelRegistration(int eventId, int patronId) {
        if (eventId == 0 || patronId == 0) {
            throw new IllegalArgumentException("Event ID and Patron ID must not be null");
        }
        EventRegistration registration = eventRegistrationRepository.findById((long) eventId, (long) patronId).orElseThrow();
        registration.setAttendanceStatus("NO_SHOW");
        return eventRegistrationRepository.save(registration);
    }

    public EventRegistration manageEventRegistration(Long eventId, Long patronId) {
        if (eventId == null || patronId == null) {
            throw new IllegalArgumentException("Event ID and Patron ID must not be null");
        }
        if (eventRegistrationRepository.existsByEventIdAndPatronId(eventId, patronId)) {
            throw new IllegalArgumentException("Patron is already registered for this event");
        }
        LibraryEvent event = libraryEventRepository.findById(eventId).orElseThrow();
        if (event.getParticipants() >= event.getMaxParticipants()) {
            throw new IllegalArgumentException("Event is full");
        }
        Patron patron = patronRepository.findById(patronId).orElseThrow();
        EventRegistration registration = new EventRegistration(event, patron, LocalDateTime.now(), "REGISTERED");
        return eventRegistrationRepository.save(registration);
    }
}