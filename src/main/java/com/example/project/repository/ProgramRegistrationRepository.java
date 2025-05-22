package com.example.project.repository;

import com.example.project.entity.ProgramRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface ProgramRegistrationRepository extends JpaRepository<ProgramRegistration, Long> {

    @Query(value = "SELECT * FROM program_registrations WHERE id = ?", nativeQuery = true)
    Optional<ProgramRegistration> findProgramRegistrationById(@Param("id") Long id);

    @Transactional
    @Query(value = "INSERT INTO program_registrations (program_id, patron_id, payment_status, attendance_log, completion_status) VALUES (?, ?, ?, ?, ?)", nativeQuery = true)
    ProgramRegistration save(@Param("programId") Long programId, @Param("patronId") Long patronId, @Param("paymentStatus") String paymentStatus, @Param("attendanceLog") String attendanceLog, @Param("completionStatus") String completionStatus);
}