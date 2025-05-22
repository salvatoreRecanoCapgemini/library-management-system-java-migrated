package com.example.project.controller;

import com.example.project.dto.MembershipRenewalResponse;
import com.example.project.entity.MembershipRenewal;
import com.example.project.service.MembershipRenewalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MembershipRenewalController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MembershipRenewalController.class);

    private final MembershipRenewalService membershipRenewalService;

    @Autowired
    public MembershipRenewalController(MembershipRenewalService membershipRenewalService) {
        this.membershipRenewalService = membershipRenewalService;
    }

    @PostMapping("/membership-renewals/process")
    public ResponseEntity<MembershipRenewalResponse> processMembershipRenewals() {
        try {
            LOGGER.info("Initiating membership renewal and notification process");
            List<MembershipRenewal> processedRenewals = membershipRenewalService.processMembershipRenewals();
            MembershipRenewalResponse response = new MembershipRenewalResponse("Membership renewal and notification process completed successfully", processedRenewals);
            LOGGER.info("Membership renewal and notification process completed successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Error processing membership renewal and notification", e);
            MembershipRenewalResponse errorResponse = new MembershipRenewalResponse("Error processing membership renewal and notification", null);
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}