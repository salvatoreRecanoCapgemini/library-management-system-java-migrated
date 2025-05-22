package com.example.project.service;

import com.example.project.entity.AuditLog;
import com.example.project.entity.Fine;
import com.example.project.entity.Loan;
import com.example.project.entity.Notification;
import com.example.project.repository.AuditLogRepository;
import com.example.project.repository.FineRepository;
import com.example.project.repository.LoanRepository;
import com.example.project.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class OverdueItemProcessor {

    private final LoanRepository loanRepository;
    private final FineRepository fineRepository;
    private final AuditLogRepository auditLogRepository;
    private final NotificationRepository notificationRepository;

    @Autowired
    public OverdueItemProcessor(LoanRepository loanRepository, FineRepository fineRepository, AuditLogRepository auditLogRepository, NotificationRepository notificationRepository) {
        this.loanRepository = loanRepository;
        this.fineRepository = fineRepository;
        this.auditLogRepository = auditLogRepository;
        this.notificationRepository = notificationRepository;
    }

    public void processOverdueItems() {
        List<Loan> overdueLoans = loanRepository.findOverdueLoans();

        for (Loan loan : overdueLoans) {
            if (fineRepository.findByLoanIdAndStatus(loan.getLoanId(), "PENDING") == null) {
                double fineAmount = calculateFineAmount(loan);

                Fine fine = new Fine(loan.getPatronId(), loan.getLoanId(), fineAmount, new Date(), new Date(), new Date(), "PENDING");
                fineRepository.save(fine);

                loan.setStatus("OVERDUE");
                loanRepository.save(loan);

                String notificationMessage = prepareNotificationMessage(loan);

                Notification notification = new Notification(loan.getPatronId(), notificationMessage);
                notificationRepository.save(notification);
            }
        }

        List<Notification> notifications = notificationRepository.findAll();
        for (Notification notification : notifications) {
            AuditLog auditLog = new AuditLog("notifications", notification.getPatronId(), "INSERT", new Date(), notification.getMessage());
            auditLogRepository.save(auditLog);
        }

        notificationRepository.deleteAll();
    }

    public void processOverdueItems(Map<String, String> queryParameters) {
        if (!CollectionUtils.isEmpty(queryParameters)) {
            List<Loan> overdueLoans = loanRepository.findOverdueLoansByQueryParameters(queryParameters);

            for (Loan loan : overdueLoans) {
                if (fineRepository.findByLoanIdAndStatus(loan.getLoanId(), "PENDING") == null) {
                    double fineAmount = calculateFineAmount(loan);

                    Fine fine = new Fine(loan.getPatronId(), loan.getLoanId(), fineAmount, new Date(), new Date(), new Date(), "PENDING");
                    fineRepository.save(fine);

                    loan.setStatus("OVERDUE");
                    loanRepository.save(loan);

                    String notificationMessage = prepareNotificationMessage(loan);

                    Notification notification = new Notification(loan.getPatronId(), notificationMessage);
                    notificationRepository.save(notification);
                }
            }

            List<Notification> notifications = notificationRepository.findAll();
            for (Notification notification : notifications) {
                AuditLog auditLog = new AuditLog("notifications", notification.getPatronId(), "INSERT", new Date(), notification.getMessage());
                auditLogRepository.save(auditLog);
            }
        }
    }

    private double calculateFineAmount(Loan loan) {
        int daysOverdue = (int) ((new Date().getTime() - loan.getDueDate().getTime()) / (1000 * 60 * 60 * 24));
        return daysOverdue * 0.50;
    }

    private String prepareNotificationMessage(Loan loan) {
        return "Dear " + loan.getPatronName() + ", your book '" + loan.getBookTitle() + "' is " + (int) ((new Date().getTime() - loan.getDueDate().getTime()) / (1000 * 60 * 60 * 24)) + " days overdue. The fine amount is $" + calculateFineAmount(loan);
    }
}