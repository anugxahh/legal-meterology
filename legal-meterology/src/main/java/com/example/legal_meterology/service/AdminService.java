package com.example.legal_meterology.service;

import com.example.legal_meterology.entity.UserProfile;
import com.example.legal_meterology.entity.VerificationApplication;
import com.example.legal_meterology.repository.UserProfileRepository;
import com.example.legal_meterology.repository.VerificationApplicationRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class AdminService {

    private final VerificationApplicationRepository applicationRepository;
    private final UserProfileRepository userRepository;

    public AdminService(
            VerificationApplicationRepository applicationRepository,
            UserProfileRepository userRepository) {

        this.applicationRepository = applicationRepository;
        this.userRepository = userRepository;
    }

    // ----------------------------------------------------
    // 1. GET ALL PENDING APPLICATIONS
    // ----------------------------------------------------

    public List<VerificationApplication> getPendingApplications() {

        return applicationRepository
                .findByStatusOrderBySubmittedAtAsc("PENDING");
    }

    // ----------------------------------------------------
    // 2. GET ALL LMOs
    // ----------------------------------------------------

    public List<UserProfile> getAllLMOs() {

        return userRepository.findByRoleIgnoreCase("LMO");
    }

    // ----------------------------------------------------
    // 3. ASSIGN APPLICATION TO LMO
    // ----------------------------------------------------

    @Transactional
    public String assignApplicationToLMO(
            UUID applicationId,
            UUID officerId) {

        // Find application
        VerificationApplication application =
                applicationRepository.findById(applicationId)
                        .orElse(null);

        if (application == null) {
            return "Application not found";
        }

        // Find LMO
        UserProfile officer =
                userRepository.findById(officerId)
                        .orElse(null);

        if (officer == null) {
            return "LMO not found";
        }

        // Make sure selected user is actually an LMO
        if (officer.getRole() == null ||
                !"LMO".equalsIgnoreCase(officer.getRole())) {

            return "Selected user is not an LMO";
        }

        // Application should only be assigned when pending
        if (!"PENDING".equalsIgnoreCase(application.getStatus())) {

            return "Application is not pending";
        }

        // Assign officer
        application.setAssignedOfficer(officer);

        // Change status
        application.setStatus("ASSIGNED");

        // Save
        applicationRepository.save(application);

        return "Application assigned successfully";
    }

    // ----------------------------------------------------
    // 4. GET APPLICATION BY ID
    // ----------------------------------------------------

    public VerificationApplication getApplication(UUID applicationId) {

        return applicationRepository.findById(applicationId)
                .orElse(null);
    }

    // ----------------------------------------------------
    // 5. GET APPLICATIONS ASSIGNED TO AN LMO
    // ----------------------------------------------------

    public List<VerificationApplication> getApplicationsForLMO(
            UUID officerId) {

        UserProfile officer =
                userRepository.findById(officerId)
                        .orElse(null);

        if (officer == null) {
            return List.of();
        }

        return applicationRepository
                .findByAssignedOfficerOrderBySubmittedAtAsc(officer);
    }
}
