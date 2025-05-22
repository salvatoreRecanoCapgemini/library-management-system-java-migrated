package com.example.project.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "library_events")
public class LibraryEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    private Long id;

    @Column(name = "title", nullable = false)
    @NotNull
    private String title;

    @Column(name = "event_date", nullable = false)
    @NotNull
    private Timestamp eventDate;

    @Column(name = "status", nullable = false)
    @NotNull
    private String status;

    @Column(name = "max_participants")
    private Integer maxParticipants;

    @Column(name = "current_participants")
    private Integer currentParticipants;

    @OneToMany(mappedBy = "libraryEvent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventRegistration> eventRegistrations;

    @OneToMany(mappedBy = "libraryEvent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AuditLog> auditLogs;

    public LibraryEvent() {}

    public LibraryEvent(Long id, String title, Timestamp eventDate, String status, Integer maxParticipants, Integer currentParticipants) {
        this.id = id;
        this.title = title;
        this.eventDate = eventDate;
        this.status = status;
        this.maxParticipants = maxParticipants;
        this.currentParticipants = currentParticipants;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Timestamp getEventDate() {
        return eventDate;
    }

    public void setEventDate(Timestamp eventDate) {
        this.eventDate = eventDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(Integer maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public Integer getCurrentParticipants() {
        return currentParticipants;
    }

    public void setCurrentParticipants(Integer currentParticipants) {
        this.currentParticipants = currentParticipants;
    }

    public List<EventRegistration> getEventRegistrations() {
        return eventRegistrations;
    }

    public void setEventRegistrations(List<EventRegistration> eventRegistrations) {
        this.eventRegistrations = eventRegistrations;
    }

    public List<AuditLog> getAuditLogs() {
        return auditLogs;
    }

    public void setAuditLogs(List<AuditLog> auditLogs) {
        this.auditLogs = auditLogs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LibraryEvent that = (LibraryEvent) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(title, that.title) &&
                Objects.equals(eventDate, that.eventDate) &&
                Objects.equals(status, that.status) &&
                Objects.equals(maxParticipants, that.maxParticipants) &&
                Objects.equals(currentParticipants, that.currentParticipants);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, eventDate, status, maxParticipants, currentParticipants);
    }

    @Override
    public String toString() {
        return "LibraryEvent{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", eventDate=" + eventDate +
                ", status='" + status + '\'' +
                ", maxParticipants=" + maxParticipants +
                ", currentParticipants=" + currentParticipants +
                '}';
    }
}