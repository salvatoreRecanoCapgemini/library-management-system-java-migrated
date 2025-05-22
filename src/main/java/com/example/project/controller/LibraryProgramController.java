package com.example.project.controller;

import com.example.project.dto.ProgramLifecycleRequest;
import com.example.project.service.LibraryProgramService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.logging.Logger;

@RestController
public class LibraryProgramController {

    private static final Logger LOGGER = Logger.getLogger(LibraryProgramController.class.getName());

    private final LibraryProgramService libraryProgramService;

    public LibraryProgramController(LibraryProgramService libraryProgramService) {
        this.libraryProgramService = libraryProgramService;
    }

    @PostMapping("/library-programs/{programId}/manage-lifecycle")
    @PreAuthorize("hasAuthority('PROGRAM_MANAGEMENT')")
    public ResponseEntity<Void> manageProgramLifecycle(@PathVariable Long programId, 
                                                       @RequestParam @NotBlank String action, 
                                                       @Valid @RequestBody ProgramLifecycleRequest params) {
        try {
            libraryProgramService.manageProgramLifecycle(programId, action, params);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            LOGGER.severe("Validation error managing program lifecycle: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (RuntimeException e) {
            LOGGER.severe("Error managing program lifecycle: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}