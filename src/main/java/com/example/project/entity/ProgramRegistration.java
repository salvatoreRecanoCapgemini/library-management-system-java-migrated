package com.example.project.entity;

import com.example.project.entity.LibraryProgram;
import com.example.project.entity.Patron;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDefs;
import org.hibernate.annotations.TypeDef;
import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "program_registrations")
@JsonPropertyOrder({"id", "programId", "patronId", "paymentStatus", "attendanceLog", "completionStatus"})
public class ProgramRegistration {

    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "program_id", nullable = false)
    private LibraryProgram libraryProgram;

    @ManyToOne
    @JoinColumn(name = "patron_id", nullable = false)
    private Patron patron;

    @Column(name = "payment_status")
    private String paymentStatus;

    @Type(type = "jsonb")
    @Column(name = "attendance_log", columnDefinition = "jsonb")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object attendanceLog;

    @Column(name = "completion_status")
    private String completionStatus;

    public ProgramRegistration() {}

    public ProgramRegistration(LibraryProgram libraryProgram, Patron patron, String paymentStatus, Object attendanceLog, String completionStatus) {
        this.libraryProgram = libraryProgram;
        this.patron = patron;
        this.paymentStatus = paymentStatus;
        this.attendanceLog = attendanceLog;
        this.completionStatus = completionStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LibraryProgram getLibraryProgram() {
        return libraryProgram;
    }

    public void setLibraryProgram(LibraryProgram libraryProgram) {
        this.libraryProgram = libraryProgram;
    }

    public Patron getPatron() {
        return patron;
    }

    public void setPatron(Patron patron) {
        this.patron = patron;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Object getAttendanceLog() {
        return attendanceLog;
    }

    public void setAttendanceLog(Object attendanceLog) {
        this.attendanceLog = attendanceLog;
    }

    public String getCompletionStatus() {
        return completionStatus;
    }

    public void setCompletionStatus(String completionStatus) {
        this.completionStatus = completionStatus;
    }
}