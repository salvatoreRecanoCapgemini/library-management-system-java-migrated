package com.example.project.controller;

import com.example.project.dto.UpdateStaffStatusRequest;
import com.example.project.entity.Staff;
import com.example.project.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class StaffController {

    private final StaffService staffService;

    @Autowired
    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    @PatchMapping("/staff/{staffId}/status")
    public ResponseEntity<Staff> updateStaffStatus(@PathVariable Long staffId, @Valid @RequestBody UpdateStaffStatusRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errorMessages = bindingResult.getFieldErrors().stream()
                    .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(new Staff());
        }

        try {
            Staff updatedStaff = staffService.updateStaffStatus(staffId, request.getNewStatus());
            return ResponseEntity.ok(updatedStaff);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Staff());
        }
    }
}