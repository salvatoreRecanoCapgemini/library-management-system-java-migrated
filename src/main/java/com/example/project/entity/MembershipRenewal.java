package com.example.project.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity
@Table(name = "membership_renewals")
public class MembershipRenewal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long membershipId;

    private Long patronId;

    private Long planId;

    private Date endDate;

    private Boolean autoRenewal;

    private Boolean hasPendingFines;

    private Integer overdueItemsCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patronId", insertable = false, updatable = false)
    private PatronMembership patronMembership;

    public MembershipRenewal() {}

    public MembershipRenewal(Long patronId, Long planId, Date endDate, Boolean autoRenewal, Boolean hasPendingFines, Integer overdueItemsCount) {
        this.patronId = patronId;
        this.planId = planId;
        this.endDate = endDate;
        this.autoRenewal = autoRenewal;
        this.hasPendingFines = hasPendingFines;
        this.overdueItemsCount = overdueItemsCount;
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

    public Boolean getHasPendingFines() {
        return hasPendingFines;
    }

    public void setHasPendingFines(Boolean hasPendingFines) {
        this.hasPendingFines = hasPendingFines;
    }

    public Integer getOverdueItemsCount() {
        return overdueItemsCount;
    }

    public void setOverdueItemsCount(Integer overdueItemsCount) {
        this.overdueItemsCount = overdueItemsCount;
    }

    public PatronMembership getPatronMembership() {
        return patronMembership;
    }

    public void setPatronMembership(PatronMembership patronMembership) {
        this.patronMembership = patronMembership;
    }
}