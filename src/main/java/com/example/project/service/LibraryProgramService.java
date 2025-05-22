package com.example.project.service;

import com.example.project.entity.LibraryProgram;
import com.example.project.entity.ProgramRegistration;
import com.example.project.entity.AuditLog;
import com.example.project.entity.Patron;
import com.example.project.repository.LibraryProgramRepository;
import com.example.project.repository.ProgramRegistrationRepository;
import com.example.project.repository.PatronRepository;
import com.example.project.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.json.JSONObject;
import org.json.JSONB;

import java.util.List;
import java.util.Map;

@Service
public class LibraryProgramService {

    private final LibraryProgramRepository libraryProgramRepository;
    private final ProgramRegistrationRepository programRegistrationRepository;
    private final PatronRepository patronRepository;
    private final AuditLogRepository auditLogRepository;

    @Autowired
    public LibraryProgramService(LibraryProgramRepository libraryProgramRepository, ProgramRegistrationRepository programRegistrationRepository, PatronRepository patronRepository, AuditLogRepository auditLogRepository) {
        this.libraryProgramRepository = libraryProgramRepository;
        this.programRegistrationRepository = programRegistrationRepository;
        this.patronRepository = patronRepository;
        this.auditLogRepository = auditLogRepository;
    }

    @Transactional
    public void manageProgramLifecycle(Long programId, String action, JSONB params) {
        LibraryProgram program = libraryProgramRepository.findById(programId).orElseThrow();
        if (!program.isPublished() || program.getRegistrations().size() < program.getMinParticipants()) {
            notifyWaitlistedParticipants(program);
            return;
        }
        if (action.equals("START_PROGRAM")) {
            startProgram(program);
            notifyRegisteredParticipants(program);
        } else if (action.equals("RECORD_ATTENDANCE")) {
            updateAttendanceLogs(program, params);
            generateAttendanceNotifications(program, params);
        } else if (action.equals("COMPLETE_PROGRAM")) {
            calculateCompletionStatistics(program);
            updateCompletionStatusForParticipants(program);
            updateProgramStatus(program, "COMPLETED");
        }
        logProgramStateChange(programId, action, params);
    }

    public Map<String, Object> getProgramMetrics(Long programId) {
        LibraryProgram program = libraryProgramRepository.findById(programId).orElseThrow();
        Map<String, Object> metrics = new JSONObject();
        metrics.put("status", program.getStatus());
        metrics.put("sessionSchedule", program.getSessionSchedule());
        metrics.put("minParticipants", program.getMinParticipants());
        metrics.put("paidRegistrations", program.getRegistrations().stream().filter(registration -> registration.isPaid()).count());
        metrics.put("totalRegistrations", program.getRegistrations().size());
        return metrics;
    }

    @Transactional
    public void updateProgramStatus(Long programId, String status) {
        LibraryProgram program = libraryProgramRepository.findById(programId).orElseThrow();
        program.setStatus(status);
        libraryProgramRepository.save(program);
    }

    @Transactional
    public void updateCompletionStatus(Long programId, Long registrationId, String completionStatus) {
        ProgramRegistration registration = programRegistrationRepository.findById(registrationId).orElseThrow();
        registration.setCompletionStatus(completionStatus);
        programRegistrationRepository.save(registration);
    }

    public void generateAttendanceNotification(Long programId, Long registrationId, JSONB attendanceLog) {
        ProgramRegistration registration = programRegistrationRepository.findById(registrationId).orElseThrow();
        Patron patron = patronRepository.findById(registration.getPatronId()).orElseThrow();
        // implement notification generation logic using patron and attendanceLog
    }

    @Transactional
    public void logProgramStateChange(Long programId, String action, JSONB params) {
        AuditLog log = new AuditLog();
        log.setProgramId(programId);
        log.setAction(action);
        log.setParams(params);
        auditLogRepository.save(log);
    }

    private void notifyWaitlistedParticipants(LibraryProgram program) {
        // implement notification logic for waitlisted participants
    }

    private void startProgram(LibraryProgram program) {
        // implement program start logic
    }

    private void notifyRegisteredParticipants(LibraryProgram program) {
        // implement notification logic for registered participants
    }

    private void updateAttendanceLogs(LibraryProgram program, JSONB params) {
        // implement attendance log update logic
    }

    private void generateAttendanceNotifications(LibraryProgram program, JSONB params) {
        // implement notification generation logic for attendance
    }

    private void calculateCompletionStatistics(LibraryProgram program) {
        // implement completion statistics calculation logic
    }

    private void updateCompletionStatusForParticipants(LibraryProgram program) {
        // implement completion status update logic for participants
    }
}