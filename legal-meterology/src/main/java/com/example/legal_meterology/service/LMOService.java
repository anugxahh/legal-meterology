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
public class LMOService {

    private final VerificationApplicationRepository applicationRepository;
    private final UserProfileRepository userRepository;

    public LMOService(
            VerificationApplicationRepository applicationRepository,
            UserProfileRepository userRepository) {

        this.applicationRepository = applicationRepository;
        this.userRepository = userRepository;
    }

    // ----------------------------------------------------
    // 1. GET TASKS ASSIGNED TO LOGGED-IN LMO
    // ----------------------------------------------------

    public List<VerificationApplication> getMyTasks(String email) {

        UserProfile officer = userRepository.findByEmail(email)
                .orElse(null);

        if (officer == null) {
            return List.of();
        }

        if (officer.getRole() == null ||
                !"LMO".equalsIgnoreCase(officer.getRole())) {

            return List.of();
        }

        return applicationRepository
                .findByAssignedOfficerOrderBySubmittedAtAsc(officer);
    }

    // ----------------------------------------------------
    // 2. GET ONE TASK
    // ----------------------------------------------------

    public VerificationApplication getMyTask(
            UUID applicationId,
            String email) {

        UserProfile officer = userRepository.findByEmail(email)
                .orElse(null);

        if (officer == null) {
            return null;
        }

        return applicationRepository
                .findByIdAndAssignedOfficer(applicationId, officer)
                .orElse(null);
    }

    // ----------------------------------------------------
    // 3. START VERIFICATION
    // ----------------------------------------------------

    @Transactional
    public String startVerification(
            UUID applicationId,
            String email) {

        UserProfile officer = userRepository.findByEmail(email)
                .orElse(null);

        if (officer == null) {
            return "LMO account not found";
        }

        VerificationApplication application =
                applicationRepository
                        .findByIdAndAssignedOfficer(
                                applicationId,
                                officer)
                        .orElse(null);

        if (application == null) {
            return "Application not found or not assigned to you";
        }

        if (!"ASSIGNED".equalsIgnoreCase(application.getStatus())) {
            return "Application is not in ASSIGNED status";
        }

        application.setStatus("IN_PROGRESS");

        applicationRepository.save(application);

        return "Verification started successfully";
    }

    // ----------------------------------------------------
    // 4. VERIFY APPLICATION
    // ----------------------------------------------------

    @Transactional
    public String verifyApplication(
            UUID applicationId,
            double observedValue,
            String remarks,
            String email) {

        UserProfile officer = userRepository.findByEmail(email)
                .orElse(null);

        if (officer == null) {
            return "LMO account not found";
        }

        VerificationApplication application =
                applicationRepository
                        .findByIdAndAssignedOfficer(
                                applicationId,
                                officer)
                        .orElse(null);

        if (application == null) {
            return "Application not found or not assigned to you";
        }

        if (!"IN_PROGRESS".equalsIgnoreCase(application.getStatus())) {
            return "Verification has not been started";
        }

        if (application.getInstrument() == null) {
            return "Instrument information is missing";
        }

        Double permissibleLimit =
                application.getInstrument().getPermissibleLimit();

        if (permissibleLimit == null) {
            return "Permissible limit is not configured";
        }

        // ------------------------------------------------
        // SYSTEM CALCULATES PASS / FAIL
        // LMO DOES NOT CHOOSE THE RESULT
        // ------------------------------------------------

        String result;

        if (Math.abs(observedValue) <= permissibleLimit) {
            result = "PASS";
        } else {
            result = "FAIL";
        }

        application.setObservedValue(observedValue);
        application.setResult(result);
        application.setRemarks(remarks);

        application.setStatus("VERIFIED");

        applicationRepository.save(application);

        return result;
    }
}
