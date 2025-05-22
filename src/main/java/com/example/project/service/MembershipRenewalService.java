package com.example.project.service;

import com.example.project.entity.AuditLog;
import com.example.project.entity.MembershipRenewal;
import com.example.project.entity.Patron;
import com.example.project.repository.AuditLogRepository;
import com.example.project.repository.MembershipRenewalRepository;
import com.example.project.service.PatronService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@Service
public class MembershipRenewalService {

    private final MembershipRenewalRepository membershipRenewalRepository;
    private final AuditLogRepository auditLogRepository;
    private final PatronService patronService;

    public MembershipRenewalService(MembershipRenewalRepository membershipRenewalRepository, AuditLogRepository auditLogRepository, PatronService patronService) {
        this.membershipRenewalRepository = membershipRenewalRepository;
        this.auditLogRepository = auditLogRepository;
        this.patronService = patronService;
    }

    public void processMembershipRenewals() {
        try {
            List<MembershipRenewal> expiringMemberships = membershipRenewalRepository.findAllByEndDateBetweenAndStatusAndAutoRenewal(LocalDate.now(), LocalDate.now().plusDays(30), "ACTIVE", true);
            for (MembershipRenewal membershipRenewal : expiringMemberships) {
                if (membershipRenewal.getAutoRenewal()) {
                    if (attemptPaymentProcessing(membershipRenewal)) {
                        MembershipRenewal newMembershipPeriod = createNewMembershipPeriod(membershipRenewal);
                        updateOldMembership(membershipRenewal);
                        String notification = prepareNotification(membershipRenewal, "Payment successful");
                        logNotification(notification);
                    } else {
                        String notification = prepareNotification(membershipRenewal, "Payment failed");
                        logNotification(notification);
                    }
                }
            }
        } catch (Exception e) {
            // Handle exception
        }
    }

    public boolean attemptPaymentProcessing(MembershipRenewal membershipRenewal) {
        Random random = new Random();
        return random.nextDouble() < 0.9;
    }

    public MembershipRenewal createNewMembershipPeriod(MembershipRenewal membershipRenewal) {
        MembershipRenewal newMembershipPeriod = new MembershipRenewal();
        newMembershipPeriod.setPatron(membershipRenewal.getPatron());
        newMembershipPeriod.setPlan(membershipRenewal.getPlan());
        newMembershipPeriod.setStartDate(LocalDate.now());
        newMembershipPeriod.setEndDate(LocalDate.now().plusDays(365));
        return membershipRenewalRepository.save(newMembershipPeriod);
    }

    public void updateOldMembership(MembershipRenewal membershipRenewal) {
        membershipRenewal.setStatus("EXPIRED");
        membershipRenewalRepository.save(membershipRenewal);
    }

    public String prepareNotification(MembershipRenewal membershipRenewal, String paymentStatus) {
        Patron patron = patronService.getPatron(membershipRenewal.getPatron().getId());
        return "Membership renewal notification for " + patron.getName() + ": " + paymentStatus;
    }

    public void logNotification(String notification) {
        AuditLog auditLog = new AuditLog();
        auditLog.setNotification(notification);
        auditLogRepository.save(auditLog);
    }
}