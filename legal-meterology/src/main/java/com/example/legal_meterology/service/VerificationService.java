package com.example.legal_meterology.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.legal_meterology.certificate.CertificateGenerationService;
import com.example.legal_meterology.certificate.CertificateResult;
import com.example.legal_meterology.entity.VerificationApplication;
import com.example.legal_meterology.repository.VerificationApplicationRepository;

@Service
public class VerificationService {

    private final VerificationApplicationRepository applicationRepository;
    private final CertificateGenerationService certificateGenerationService;

    public VerificationService(
            VerificationApplicationRepository applicationRepository,
            CertificateGenerationService certificateGenerationService) {

        this.applicationRepository = applicationRepository;
        this.certificateGenerationService = certificateGenerationService;
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

        // Generate certificate automatically only if verification passed
        if ("PASS".equals(result)) {

            CertificateResult certificateResult =
                    certificateGenerationService
                            .generateCertificate(applicationId);

            if (!certificateResult.isSuccess()) {
                return "PASS, but certificate generation failed: "
                        + certificateResult.getMessage();
            }
        }

        return result;
    }
}