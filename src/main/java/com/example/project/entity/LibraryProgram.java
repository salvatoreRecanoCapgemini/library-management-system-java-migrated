package com.example.project.entity;

import com.example.project.entity.ProgramRegistration;
import com.example.project.entity.Patron;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDefs;
import org.hibernate.annotations.TypeDef;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "library_programs")
@JsonIgnoreProperties(ignoreUnknown = true)
public class LibraryProgram {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @NotNull
    @NotEmpty
    private String name;

    @Column(name = "status")
    @NotNull
    @NotEmpty
    private String status;

    @Column(name = "session_schedule")
    @Type(type = "jsonb")
    private String sessionSchedule;

    @Column(name = "min_participants")
    @NotNull
    private Integer minParticipants;

    @Column(name = "paid_registrations")
    @NotNull
    private Integer paidRegistrations;

    @Column(name = "total_registrations")
    @NotNull
    private Integer totalRegistrations;

    @Column(name = "end_date")
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date endDate;

    @OneToMany(mappedBy = "libraryProgram")
    private List<ProgramRegistration> programRegistrations;

    @ManyToMany
    @JoinTable(
            name = "library_program_patron",
            joinColumns = @JoinColumn(name = "library_program_id"),
            inverseJoinColumns = @JoinColumn(name = "patron_id")
    )
    private Set<Patron> patrons;

    public LibraryProgram() {}

    public LibraryProgram(Long id, String name, String status, String sessionSchedule, Integer minParticipants, Integer paidRegistrations, Integer totalRegistrations, Date endDate) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.sessionSchedule = sessionSchedule;
        this.minParticipants = minParticipants;
        this.paidRegistrations = paidRegistrations;
        this.totalRegistrations = totalRegistrations;
        this.endDate = endDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSessionSchedule() {
        return sessionSchedule;
    }

    public void setSessionSchedule(String sessionSchedule) {
        this.sessionSchedule = sessionSchedule;
    }

    public Integer getMinParticipants() {
        return minParticipants;
    }

    public void setMinParticipants(Integer minParticipants) {
        this.minParticipants = minParticipants;
    }

    public Integer getPaidRegistrations() {
        return paidRegistrations;
    }

    public void setPaidRegistrations(Integer paidRegistrations) {
        this.paidRegistrations = paidRegistrations;
    }

    public Integer getTotalRegistrations() {
        return totalRegistrations;
    }

    public void setTotalRegistrations(Integer totalRegistrations) {
        this.totalRegistrations = totalRegistrations;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public List<ProgramRegistration> getProgramRegistrations() {
        return programRegistrations;
    }

    public void setProgramRegistrations(List<ProgramRegistration> programRegistrations) {
        this.programRegistrations = programRegistrations;
    }

    public Set<Patron> getPatrons() {
        return patrons;
    }

    public void setPatrons(Set<Patron> patrons) {
        this.patrons = patrons;
    }
}