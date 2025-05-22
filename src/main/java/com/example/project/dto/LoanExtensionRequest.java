package com.example.project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.Positive;

public class LoanExtensionRequest {

    @JsonProperty("extensionDays")
    @Positive(message = "Extension days must be a positive integer")
    private Integer extensionDays;

    public LoanExtensionRequest(Integer extensionDays) {
        this.extensionDays = extensionDays;
    }

    public Integer getExtensionDays() {
        return extensionDays;
    }

    public void setExtensionDays(Integer extensionDays) {
        this.extensionDays = extensionDays;
    }
}