package com.example.project.controller;

import com.example.project.dto.ProgramLifecycleRequest;
import com.example.project.service.LibraryProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LibraryProgramLifecycleController {

    private final LibraryProgramService libraryProgramService;

    @Autowired
    public LibraryProgramLifecycleController(LibraryProgramService libraryProgramService) {
        this.libraryProgramService = libraryProgramService;
    }

    @PostMapping("/library-program-lifecycle")
    public ResponseEntity<Object> manageProgramLifecycle(@RequestBody ProgramLifecycleRequest request) {
        try {
            String programId = request.getProgramId();
            String action = request.getAction();
            Object params = request.getParams();

            if (programId == null || programId.isEmpty() || !isValidAction(action) || params == null) {
                return ResponseEntity.badRequest().body("Invalid request");
            }

            Object result = libraryProgramService.manageProgramLifecycle(programId, action, params);
            return ResponseEntity.ok(new ProgramLifecycleResponse("Program lifecycle managed successfully", result));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Error managing program lifecycle", e.getMessage()));
        }
    }

    private boolean isValidAction(String action) {
        return action.equals("START_PROGRAM") || action.equals("RECORD_ATTENDANCE") || action.equals("COMPLETE_PROGRAM");
    }

    private static class ProgramLifecycleResponse {
        private String message;
        private Object data;

        public ProgramLifecycleResponse(String message, Object data) {
            this.message = message;
            this.data = data;
        }

        public String getMessage() {
            return message;
        }

        public Object getData() {
            return data;
        }
    }

    private static class ErrorResponse {
        private String message;
        private String error;

        public ErrorResponse(String message, String error) {
            this.message = message;
            this.error = error;
        }

        public String getMessage() {
            return message;
        }

        public String getError() {
            return error;
        }
    }
}