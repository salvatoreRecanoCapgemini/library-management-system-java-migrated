package com.example.project.dto;

import com.example.project.service.LibraryProgramService;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgramLifecycleRequest {

    @NotNull
    @JsonProperty("programId")
    private Long programId;

    @NotNull
    @JsonProperty("action")
    private String action;

    @NotNull
    @JsonProperty("params")
    private Map<String, String> params;

    public void validate() {
        if (programId == null || programId <= 0) {
            throw new IllegalArgumentException("Invalid programId");
        }

        if (!action.equals(ProgramLifecycleAction.START_PROGRAM.toString())
                && !action.equals(ProgramLifecycleAction.RECORD_ATTENDANCE.toString())
                && !action.equals(ProgramLifecycleAction.COMPLETE_PROGRAM.toString())) {
            throw new IllegalArgumentException("Invalid action");
        }
    }

    public enum ProgramLifecycleAction {
        START_PROGRAM,
        RECORD_ATTENDANCE,
        COMPLETE_PROGRAM
    }

    public void performAction(ProgramLifecycleAction action) {
        switch (action) {
            case START_PROGRAM:
                ProgramLifecycleService.startProgram(programId, params);
                break;
            case RECORD_ATTENDANCE:
                ProgramLifecycleService.recordAttendance(programId, params);
                break;
            case COMPLETE_PROGRAM:
                ProgramLifecycleService.completeProgram(programId, params);
                break;
            default:
                throw new IllegalArgumentException("Invalid action");
        }
    }
}