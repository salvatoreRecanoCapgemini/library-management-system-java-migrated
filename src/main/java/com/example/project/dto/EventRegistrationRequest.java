package com.example.project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotNull;

public class EventRegistrationRequest {

    @JsonProperty("patronId")
    @NotNull
    private Long patronId;

    public EventRegistrationRequest(@NotNull Long patronId) {
        this.patronId = patronId;
    }

    public Long getPatronId() {
        return patronId;
    }

    public void setPatronId(@NotNull Long patronId) {
        this.patronId = patronId;
    }
}