package com.example.legal_meterology.controller;

import com.example.legal_meterology.entity.VerificationApplication;
import com.example.legal_meterology.repository.VerificationApplicationRepository;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/applications")
@CrossOrigin(origins = "*")
public class ApplicationController {

    private final VerificationApplicationRepository applicationRepository;

    public ApplicationController(
            VerificationApplicationRepository applicationRepository) {

        this.applicationRepository = applicationRepository;
    }

    @PostMapping
    public VerificationApplication createApplication(
            @RequestBody VerificationApplication application) {

        application.setStatus("PENDING");

        return applicationRepository.save(application);
    }

    @GetMapping
    public List<VerificationApplication> getAllApplications() {
        return applicationRepository.findAll();
    }

    @GetMapping("/{id}")
    public VerificationApplication getApplication(
            @PathVariable UUID id) {

        return applicationRepository.findById(id).orElse(null);
    }

    @DeleteMapping("/{id}")
    public String deleteApplication(@PathVariable UUID id) {

        if (!applicationRepository.existsById(id)) {
            return "Application not found";
        }

        applicationRepository.deleteById(id);

        return "Application deleted successfully";
    }
}
