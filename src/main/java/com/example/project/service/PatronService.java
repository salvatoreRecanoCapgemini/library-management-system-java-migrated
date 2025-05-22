package com.example.project.service;

import com.example.project.dto.PatronCreateRequest;
import com.example.project.entity.Patron;
import com.example.project.exception.ServiceException;
import com.example.project.exception.ValidationException;
import com.example.project.repository.PatronRepository;
import com.example.project.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PatronService {

    private final PatronRepository patronRepository;
    private final Validator validator;

    @Autowired
    public PatronService(PatronRepository patronRepository, Validator validator) {
        this.patronRepository = patronRepository;
        this.validator = validator;
    }

    public void createPatron(String firstName, String lastName, String email, String phone, Date birthDate) {
        validator.validate(firstName, lastName, email, phone, birthDate);
        if (!validator.isValid()) {
            throw new ValidationException("Invalid input data");
        }
        Patron patron = new Patron();
        patron.setFirstName(firstName);
        patron.setLastName(lastName);
        patron.setEmail(email);
        patron.setPhone(phone);
        patron.setBirthDate(birthDate);
        patron.setMembershipDate(new Date());
        patron.setStatus("ACTIVE");
        try {
            patronRepository.save(patron);
        } catch (Exception e) {
            throw new ServiceException("Failed to create patron", e);
        }
    }

    public Long createPatron(PatronCreateRequest patronCreateRequest) {
        validator.validate(patronCreateRequest);
        if (!validator.isValid()) {
            throw new ValidationException("Invalid input data");
        }
        Patron patron = new Patron();
        patron.setFirstName(patronCreateRequest.getFirstName());
        patron.setLastName(patronCreateRequest.getLastName());
        patron.setEmail(patronCreateRequest.getEmail());
        patron.setPhone(patronCreateRequest.getPhone());
        patron.setBirthDate(patronCreateRequest.getBirthDate());
        patron.setMembershipDate(new Date());
        patron.setStatus("ACTIVE");
        try {
            Patron savedPatron = patronRepository.save(patron);
            return savedPatron.getId();
        } catch (Exception e) {
            throw new ServiceException("Failed to create patron", e);
        }
    }

    public Patron getPatronByEmail(String email) {
        try {
            return patronRepository.findByEmail(email);
        } catch (Exception e) {
            throw new ServiceException("Failed to retrieve patron by email", e);
        }
    }

    public Patron getPatronById(Integer id) {
        try {
            return patronRepository.findById(id);
        } catch (Exception e) {
            throw new ServiceException("Failed to retrieve patron by id", e);
        }
    }

    public String getEmail(Long patronId) {
        try {
            return patronRepository.getEmail(patronId);
        } catch (Exception e) {
            throw new ServiceException("Failed to retrieve patron email", e);
        }
    }
}