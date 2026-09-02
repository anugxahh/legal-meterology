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

    // POST: Create a new verification application
    @PostMapping
    public VerificationApplication createApplication(
            @RequestBody VerificationApplication application) {

        return applicationRepository.save(application);
    }

    // GET: Get all applications
    @GetMapping
    public List<VerificationApplication> getAllApplications() {
        return applicationRepository.findAll();
    }

    // GET: Get one application by ID
    @GetMapping("/{id}")
    public VerificationApplication getApplication(
            @PathVariable UUID id) {

        return applicationRepository.findById(id).orElse(null);
    }

    // DELETE: Delete an application
    @DeleteMapping("/{id}")
    public String deleteApplication(
            @PathVariable UUID id) {

        applicationRepository.deleteById(id);
        return "Application deleted successfully";
    }
}
