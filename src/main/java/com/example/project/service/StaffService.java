package com.example.project.service;

import com.example.project.entity.Staff;
import com.example.project.repository.StaffRepository;
import org.springframework.stereotype.Service;

@Service
public class StaffService {

    private final StaffRepository staffRepository;

    public StaffService(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    public Staff updateStaffStatus(Long staffId, String newStatus) {
        try {
            staffRepository.updateStatus(staffId, newStatus);
            return staffRepository.findById(staffId).orElseThrow();
        } catch (Exception e) {
            throw new RuntimeException("Error updating staff status", e);
        }
    }
}