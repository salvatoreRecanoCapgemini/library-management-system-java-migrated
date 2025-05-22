package com.example.project.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "patron_memberships", uniqueConstraints = @UniqueConstraint(columnNames = "membershipId"), indexes = {@Index(columnList = "patronId"), @Index(columnList = "planId")})
public class PatronMembership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long membershipId;

    @NotNull
    @Column
    private Long patronId;

    @NotNull
    @Column
    private Long planId;

    @NotNull
    @Column
    private Date startDate;

    @NotNull
    @Column
    private Date endDate;

    @NotNull
    @Column
    private Boolean autoRenewal;

    @NotNull
    @Size(min = 1, max = 10)
    @Column
    private String status;

    @OneToOne
    @JoinColumn(name = "patronId", insertable = false, updatable = false)
    private Patron patron;

    @OneToOne
    @JoinColumn(name = "planId", insertable = false, updatable = false)
    private MembershipPlan membershipPlan;

    public PatronMembership() {}

    public PatronMembership(Long patronId, Long planId, Date startDate, Date endDate, Boolean autoRenewal, String status) {
        if (patronId == null || planId == null) {
            throw new IllegalArgumentException("Patron ID and Plan ID cannot be null");
        }
        if (!isValidStatus(status)) {
            throw new IllegalArgumentException("Invalid status");
        }
        this.patronId = patronId;
        this.planId = planId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.autoRenewal = autoRenewal;
        this.status = status;
    }

    private boolean isValidStatus(String status) {
        return "ACTIVE".equals(status) || "EXPIRED".equals(status) || "PENDING".equals(status);
    }

    public Long getMembershipId() {
        return membershipId;
    }

    public void setMembershipId(Long membershipId) {
        this.membershipId = membershipId;
    }

    public Long getPatronId() {
        return patronId;
    }

    public void setPatronId(Long patronId) {
        this.patronId = patronId;
    }

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Boolean getAutoRenewal() {
        return autoRenewal;
    }

    public void setAutoRenewal(Boolean autoRenewal) {
        this.autoRenewal = autoRenewal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (!isValidStatus(status)) {
            throw new IllegalArgumentException("Invalid status");
        }
        this.status = status;
    }

    public Patron getPatron() {
        return patron;
    }

    public void setPatron(Patron patron) {
        this.patron = patron;
    }

    public MembershipPlan getMembershipPlan() {
        return membershipPlan;
    }

    public void setMembershipPlan(MembershipPlan membershipPlan) {
        this.membershipPlan = membershipPlan;
    }
}