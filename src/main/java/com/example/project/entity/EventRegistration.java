package com.example.project.entity;

import com.example.project.entity.LibraryEvent;
import com.example.project.entity.Patron;
import com.example.project.entity.AuditLog;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "event_registrations", uniqueConstraints = @UniqueConstraint(columnNames = {"event_id", "patron_id"}))
public class EventRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "event_id")
    private LibraryEvent libraryEvent;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "patron_id")
    private Patron patron;

    @NotNull
    private Date registrationDate;

    @NotNull
    @Pattern(regexp = "REGISTERED|NO_SHOW|ATTENDED", message = "Invalid attendance status")
    private String attendanceStatus;

    @OneToMany(mappedBy = "eventRegistration")
    private List<AuditLog> auditLogs;

    public EventRegistration() {}

    public EventRegistration(LibraryEvent libraryEvent, Patron patron, Date registrationDate, String attendanceStatus) {
        this.libraryEvent = libraryEvent;
        this.patron = patron;
        this.registrationDate = registrationDate;
        this.attendanceStatus = attendanceStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LibraryEvent getLibraryEvent() {
        return libraryEvent;
    }

    public void setLibraryEvent(LibraryEvent libraryEvent) {
        this.libraryEvent = libraryEvent;
    }

    public Patron getPatron() {
        return patron;
    }

    public void setPatron(Patron patron) {
        this.patron = patron;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getAttendanceStatus() {
        return attendanceStatus;
    }

    public void setAttendanceStatus(String attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
    }

    public List<AuditLog> getAuditLogs() {
        return auditLogs;
    }

    public void setAuditLogs(List<AuditLog> auditLogs) {
        this.auditLogs = auditLogs;
    }
}