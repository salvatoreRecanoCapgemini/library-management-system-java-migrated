package com.example.project.dto;

import javax.validation.constraints.NotNull;
import javax.validation.Valid;

@Valid
public class PatronCreateRequest {

    @NotNull
    private String firstName = "";

    @NotNull
    private String lastName = "";

    @NotNull
    private String email = "";

    @NotNull
    private String phone = "";

    @NotNull
    private String birthDate = "";

    public PatronCreateRequest() {}

    public PatronCreateRequest(String firstName, String lastName, String email, String phone, String birthDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.birthDate = birthDate;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }
}