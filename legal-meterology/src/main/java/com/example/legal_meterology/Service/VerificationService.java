package com.example.legal_meterology.Service;

import com.example.legal_meterology.entity.VerificationApplication;
import com.example.legal_meterology.repository.VerificationApplicationRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class VerificationService {

    private final VerificationApplicationRepository applicationRepository;

    public VerificationService(
            VerificationApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    public String verifyApplication(
            UUID applicationId,
            double observedValue) {

        // Find the application
        VerificationApplication application =
                applicationRepository.findById(applicationId).orElse(null);

        if (application == null) {
            return "Application not found";
        }

        // Get the permissible limit from the instrument
        double permissibleLimit =
                application.getInstrument().getPermissibleLimit();

        // System automatically decides PASS or FAIL
        String result;

        if (Math.abs(observedValue) <= permissibleLimit) {
            result = "PASS";
        } else {
            result = "FAIL";
        }

        // Save the verification details
        application.setObservedValue(observedValue);
        application.setResult(result);
        application.setStatus("VERIFIED");

        applicationRepository.save(application);

        return result;
    }
}
