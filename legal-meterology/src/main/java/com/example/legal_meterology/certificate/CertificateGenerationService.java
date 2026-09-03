package com.example.legal_meterology.certificate;

import java.io.File;
import java.time.LocalDate;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.legal_meterology.entity.Certificate;
import com.example.legal_meterology.entity.VerificationApplication;
import com.example.legal_meterology.repository.CertificateRepository;
import com.example.legal_meterology.repository.VerificationApplicationRepository;

@Service
public class CertificateGenerationService {

    private final QRCodeGenerator qrCodeGenerator;
    private final CertificateGenerator certificateGenerator;
    private final CertificateRepository certificateRepository;
    private final VerificationApplicationRepository applicationRepository;

    public CertificateGenerationService(
            CertificateRepository certificateRepository,
            VerificationApplicationRepository applicationRepository) {

        this.qrCodeGenerator = new QRCodeGenerator();
        this.certificateGenerator = new CertificateGenerator();
        this.certificateRepository = certificateRepository;
        this.applicationRepository = applicationRepository;
    }

    public CertificateResult generateCertificate(UUID applicationId) {

        try {

            // STEP 1: Find the verification application
            VerificationApplication application =
                    applicationRepository.findById(applicationId)
                            .orElse(null);

            if (application == null) {

                return new CertificateResult(
                        false,
                        "Verification application not found",
                        null,
                        null,
                        null
                );
            }

            // STEP 2: Certificate can only be generated if verification passed
            if (!"PASS".equalsIgnoreCase(application.getResult())) {

                return new CertificateResult(
                        false,
                        "Certificate cannot be generated because verification did not pass",
                        null,
                        null,
                        null
                );
            }

            // STEP 3: Create certificate database record
            Certificate certificate = new Certificate();

            certificate.setApplication(application);

            LocalDate issueDate = LocalDate.now();
            LocalDate validUntil = issueDate.plusYears(5);

            certificate.setIssueDate(issueDate);
            certificate.setValidUntil(validUntil);
            certificate.setStatus("VALID");

            certificate = certificateRepository.save(certificate);

            // STEP 4: Create CertificateData from backend data
            CertificateData data = new CertificateData();

            data.setFileNumber(application.getId().toString());

            data.setIssueDate(issueDate.toString());

            data.setFirmName(
                    application.getOwner().getBusinessName()
            );

            data.setFirmAddress(
                    application.getOwner().getAddress()
            );

            data.setImportedItems(
                    application.getInstrument().getType()
                            + " - "
                            + application.getInstrument().getManufacturer()
                            + ", Serial Number: "
                            + application.getInstrument().getSerialNumber()
                            + ", Capacity Range: "
                            + application.getInstrument().getCapacityRange()
            );

            // Use certificate UUID as registration number
            data.setRegistrationNumber(
                    certificate.getId().toString()
            );

            data.setValidUpto(
                    validUntil.toString()
            );

            // Fixed authority details for prototype
            data.setIssuingAuthority(
                    "Legal Metrology Department"
            );

            data.setDesignation(
                    "Legal Metrology Officer"
            );

            data.setTelephone(
                    "011 23389489"
            );

            data.setEmail(
                    "legalmetrology@example.gov.in"
            );

            // QR verification URL
            data.setVerificationUrl(
                    "http://localhost:8080/api/certificates/verify/"
                            + certificate.getId()
            );

            // STEP 5: Create output directory
            File outputDirectory = new File("generated");

            if (!outputDirectory.exists()) {
                outputDirectory.mkdirs();
            }

            // Safe filename
            String safeCertificateId =
                    certificate.getId().toString();

            String qrPath =
                    new File(
                            outputDirectory,
                            safeCertificateId + "_QR.png"
                    ).getPath();

            String pdfPath =
                    new File(
                            outputDirectory,
                            safeCertificateId + ".pdf"
                    ).getPath();

            // STEP 6: Generate QR
            qrCodeGenerator.generate(
                    data.getVerificationUrl(),
                    qrPath
            );

            // STEP 7: Generate PDF
            certificateGenerator.generate(
                    data,
                    pdfPath,
                    qrPath
            );

            // STEP 8: Return result
            return new CertificateResult(
                    true,
                    "Certificate generated successfully",
                    certificate.getId().toString(),
                    pdfPath,
                    qrPath
            );

        } catch (Exception e) {

            e.printStackTrace();

            return new CertificateResult(
                    false,
                    "Certificate generation failed: "
                            + e.getMessage(),
                    null,
                    null,
                    null
            );
        }
    }
}