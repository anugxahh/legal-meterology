package com.example.legal_meterology.service;

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

        VerificationApplication application =
                applicationRepository.findById(applicationId).orElse(null);

        if (application == null) {
            return "Application not found";
        }

        double permissibleLimit =
                application.getInstrument().getPermissibleLimit();

        String result;

        if (Math.abs(observedValue) <= permissibleLimit) {
            result = "PASS";
        } else {
            result = "FAIL";
        }

        application.setObservedValue(observedValue);
        application.setResult(result);
        application.setStatus("VERIFIED");

        applicationRepository.save(application);

        return result;
    }
}
