package com.example.project.service;

import com.example.project.entity.LibraryEvent;
import com.example.project.entity.EventRegistration;
import com.example.project.entity.Patron;
import com.example.project.repository.LibraryEventRepository;
import com.example.project.repository.EventRegistrationRepository;
import com.example.project.service.PatronService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class LibraryEventService {

    private final LibraryEventRepository libraryEventRepository;
    private final EventRegistrationRepository eventRegistrationRepository;
    private final PatronService patronService;

    @Autowired
    public LibraryEventService(LibraryEventRepository libraryEventRepository, EventRegistrationRepository eventRegistrationRepository, PatronService patronService) {
        this.libraryEventRepository = libraryEventRepository;
        this.eventRegistrationRepository = eventRegistrationRepository;
        this.patronService = patronService;
    }

    public void cancelEvent(int eventId) {
        Optional<LibraryEvent> eventOptional = libraryEventRepository.findById(eventId);
        if (eventOptional.isPresent()) {
            LibraryEvent event = eventOptional.get();
            event.setStatus("CANCELLED");
            libraryEventRepository.save(event);

            List<EventRegistration> registrations = eventRegistrationRepository.findAll();
            for (EventRegistration registration : registrations) {
                registration.setAttendanceStatus("NO_SHOW");
                eventRegistrationRepository.save(registration);
            }

            Patron patron = patronService.getPatronById(registrations.get(0).getPatronId());
            patronService.notify(patron);
        } else {
            throw new IllegalArgumentException("Event not found");
        }
    }

    public void rescheduleEvent(int eventId, LocalDate newDate) {
        if (newDate == null) {
            throw new IllegalArgumentException("New date is required for rescheduling");
        }

        Optional<LibraryEvent> eventOptional = libraryEventRepository.findById(eventId);
        if (eventOptional.isPresent()) {
            LibraryEvent event = eventOptional.get();
            if (newDate.isBefore(LocalDate.now())) {
                throw new IllegalArgumentException("New date cannot be in the past");
            }
            event.setEventDate(newDate);
            libraryEventRepository.save(event);

            List<EventRegistration> registrations = eventRegistrationRepository.findAll();
            for (EventRegistration registration : registrations) {
                patronService.notify(patronService.getPatronById(registration.getPatronId()));
            }
        } else {
            throw new IllegalArgumentException("Event not found");
        }
    }

    public void updateCurrentParticipants(Long eventId, Integer currentParticipants) {
        try {
            libraryEventRepository.updateCurrentParticipants(eventId, currentParticipants);
        } catch (Exception e) {
            throw new RuntimeException("Error updating current participants", e);
        }
    }
}