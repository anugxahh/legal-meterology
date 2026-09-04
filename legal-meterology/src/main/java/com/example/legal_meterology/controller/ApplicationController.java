package com.example.legal_meterology.controller;

import com.example.legal_meterology.entity.Instrument;
import com.example.legal_meterology.entity.UserProfile;
import com.example.legal_meterology.entity.VerificationApplication;
import com.example.legal_meterology.repository.InstrumentRepository;
import com.example.legal_meterology.repository.UserProfileRepository;
import com.example.legal_meterology.repository.VerificationApplicationRepository;
import com.example.legal_meterology.service.InstrumentValidationService;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/customer")
@CrossOrigin(origins = "*")
public class ApplicationController {

    private final VerificationApplicationRepository applicationRepository;
    private final UserProfileRepository userRepository;
    private final InstrumentRepository instrumentRepository;
    private final InstrumentValidationService instrumentValidationService;

    public ApplicationController(
            VerificationApplicationRepository applicationRepository,
            UserProfileRepository userRepository,
            InstrumentRepository instrumentRepository,
            InstrumentValidationService instrumentValidationService) {

        this.applicationRepository = applicationRepository;
        this.userRepository = userRepository;
        this.instrumentRepository = instrumentRepository;
        this.instrumentValidationService = instrumentValidationService;
    }

    // ====================================================
    // 1. CREATE CUSTOMER APPLICATION
    // ====================================================

    @PostMapping("/applications")
    @Transactional
    public ResponseEntity<?> createApplication(
            @RequestBody FrontendApplicationDTO payload,
            Principal principal) {

        try {

            // --------------------------------------------
            // Get logged-in customer from JWT
            // --------------------------------------------

            if (principal == null) {
                return ResponseEntity.status(401)
                        .body("Error: Authentication required.");
            }

            String loggedInEmail = principal.getName();

            Optional<UserProfile> userOpt =
                    userRepository.findByEmail(loggedInEmail);

            if (userOpt.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body("Error: User account not found!");
            }

            UserProfile owner = userOpt.get();

            // --------------------------------------------
            // Make sure user is a CUSTOMER
            // --------------------------------------------

            if (owner.getRole() == null ||
                    !"CUSTOMER".equalsIgnoreCase(owner.getRole())) {

                return ResponseEntity.status(403)
                        .body(
                                "Error: Only customers can create applications."
                        );
            }

            // --------------------------------------------
            // Validate request
            // --------------------------------------------

            if (payload == null) {
                return ResponseEntity.badRequest()
                        .body("Error: Application data is required.");
            }

            if (payload.getInstrument() == null) {
                return ResponseEntity.badRequest()
                        .body(
                                "Error: Instrument information is required."
                        );
            }

            InstrumentDTO instrumentData =
                    payload.getInstrument();

            // --------------------------------------------
            // Create instrument
            // --------------------------------------------

            Instrument instrument = new Instrument();

            // Instrument type
            String type = instrumentData.getType();

            if (type != null && !type.trim().isEmpty()) {
                instrument.setType(type.trim());
            } else {
                instrument.setType("Not Specified");
            }

            // Manufacturer
            String manufacturer =
                    instrumentData.getManufacturer();

            if (manufacturer != null &&
                    !manufacturer.trim().isEmpty()) {

                instrument.setManufacturer(
                        manufacturer.trim()
                );

            } else {

                instrument.setManufacturer(
                        "Not Specified"
                );
            }

            // Serial number
            String serialNumber =
                    instrumentData.getSerialNumber();

            if (serialNumber != null &&
                    !serialNumber.trim().isEmpty()) {

                instrument.setSerialNumber(
                        serialNumber.trim()
                );

            } else {

                instrument.setSerialNumber(
                        UUID.randomUUID().toString()
                );
            }

            // Capacity
            String capacity =
                    instrumentData.getCapacity();

            if (capacity != null &&
                    !capacity.trim().isEmpty()) {

                instrument.setCapacityRange(
                        capacity.trim()
                );

            } else {

                instrument.setCapacityRange(
                        "Not Specified"
                );
            }

            // --------------------------------------------
            // Calculate permissible limit
            // --------------------------------------------

            double permissibleLimit =
                    instrumentValidationService
                            .getPermissibleLimit(
                                    instrument.getType()
                            );

            instrument.setPermissibleLimit(
                    permissibleLimit
            );

            // --------------------------------------------
            // Save instrument
            // --------------------------------------------

            instrument =
                    instrumentRepository.save(instrument);

            // --------------------------------------------
            // Create verification application
            // --------------------------------------------

            VerificationApplication application =
                    new VerificationApplication();

            application.setOwner(owner);
            application.setInstrument(instrument);

            // Application starts in PENDING state
            application.setStatus("PENDING");

            // submittedAt is automatically created
            // by VerificationApplication constructor

            applicationRepository.save(application);

            return ResponseEntity.ok(
                    "Success: Application submitted successfully!"
            );

        } catch (DataIntegrityViolationException e) {

            System.err.println(
                    "DATABASE ERROR: Duplicate Serial Number or Missing Data."
            );

            return ResponseEntity.badRequest()
                    .body(
                            "Error: This Serial Number is already registered in the system."
                    );

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity.internalServerError()
                    .body(
                            "Error: An unexpected server error occurred."
                    );
        }
    }

    // ====================================================
    // 2. CUSTOMER DASHBOARD
    // ====================================================

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Long>> getDashboardStats(
            Principal principal) {

        if (principal == null) {
            return ResponseEntity.status(401).build();
        }

        String loggedInEmail =
                principal.getName();

        Optional<UserProfile> userOpt =
                userRepository.findByEmail(
                        loggedInEmail
                );

        if (userOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        UserProfile owner =
                userOpt.get();

        List<VerificationApplication> myApps =
                applicationRepository
                        .findByOwnerOrderBySubmittedAtDesc(
                                owner
                        );

        long pending =
                myApps.stream()
                        .filter(a ->
                                "PENDING".equalsIgnoreCase(
                                        a.getStatus()
                                )
                        )
                        .count();

        long assigned =
                myApps.stream()
                        .filter(a ->
                                "ASSIGNED".equalsIgnoreCase(
                                        a.getStatus()
                                )
                        )
                        .count();

        long inProgress =
                myApps.stream()
                        .filter(a ->
                                "IN_PROGRESS".equalsIgnoreCase(
                                        a.getStatus()
                                )
                        )
                        .count();

        long verified =
                myApps.stream()
                        .filter(a ->
                                "VERIFIED".equalsIgnoreCase(
                                        a.getStatus()
                                )
                        )
                        .count();

        Map<String, Long> stats =
                new HashMap<>();

        stats.put(
                "totalApplications",
                (long) myApps.size()
        );

        stats.put(
                "pending",
                pending
        );

        stats.put(
                "assigned",
                assigned
        );

        stats.put(
                "inProgress",
                inProgress
        );

        stats.put(
                "verified",
                verified
        );

        return ResponseEntity.ok(stats);
    }

    // ====================================================
    // 3. GET CUSTOMER APPLICATIONS
    // ====================================================

    @GetMapping("/applications")
    public ResponseEntity<List<VerificationApplication>>
    getMyApplications(Principal principal) {

        if (principal == null) {
            return ResponseEntity.status(401).build();
        }

        String loggedInEmail =
                principal.getName();

        Optional<UserProfile> userOpt =
                userRepository.findByEmail(
                        loggedInEmail
                );

        if (userOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        UserProfile owner =
                userOpt.get();

        List<VerificationApplication> applications =
                applicationRepository
                        .findByOwnerOrderBySubmittedAtDesc(
                                owner
                        );

        return ResponseEntity.ok(applications);
    }

    // ====================================================
    // 4. GET ONE CUSTOMER APPLICATION
    // ====================================================

    @GetMapping("/applications/{id}")
    public ResponseEntity<?> getApplication(
            @PathVariable UUID id,
            Principal principal) {

        if (principal == null) {
            return ResponseEntity.status(401)
                    .body("Authentication required.");
        }

        String loggedInEmail =
                principal.getName();

        Optional<UserProfile> userOpt =
                userRepository.findByEmail(
                        loggedInEmail
                );

        if (userOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        UserProfile owner =
                userOpt.get();

        Optional<VerificationApplication> applicationOpt =
                applicationRepository.findById(id);

        if (applicationOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        VerificationApplication application =
                applicationOpt.get();

        // --------------------------------------------
        // Security check
        // --------------------------------------------

        if (application.getOwner() == null ||
                application.getOwner().getId() == null ||
                !application.getOwner()
                        .getId()
                        .equals(owner.getId())) {

            return ResponseEntity.status(403)
                    .body(
                            "Error: You are not allowed to access this application."
                    );
        }

        return ResponseEntity.ok(application);
    }

    // ====================================================
    // 5. DELETE CUSTOMER APPLICATION
    // ====================================================

    @DeleteMapping("/applications/{id}")
    @Transactional
    public ResponseEntity<String> deleteApplication(
            @PathVariable UUID id,
            Principal principal) {

        if (principal == null) {
            return ResponseEntity.status(401)
                    .body("Authentication required.");
        }

        String loggedInEmail =
                principal.getName();

        Optional<UserProfile> userOpt =
                userRepository.findByEmail(
                        loggedInEmail
                );

        if (userOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        UserProfile owner =
                userOpt.get();

        Optional<VerificationApplication> applicationOpt =
                applicationRepository.findById(id);

        if (applicationOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        VerificationApplication application =
                applicationOpt.get();

        // --------------------------------------------
        // Security check
        // --------------------------------------------

        if (application.getOwner() == null ||
                application.getOwner().getId() == null ||
                !application.getOwner()
                        .getId()
                        .equals(owner.getId())) {

            return ResponseEntity.status(403)
                    .body(
                            "Error: You are not allowed to delete this application."
                    );
        }

        // --------------------------------------------
        // Only pending applications can be deleted
        // --------------------------------------------

        if (!"PENDING".equalsIgnoreCase(
                application.getStatus())) {

            return ResponseEntity.badRequest()
                    .body(
                            "Error: Only pending applications can be deleted."
                    );
        }

        applicationRepository.delete(application);

        return ResponseEntity.ok(
                "Application deleted successfully"
        );
    }

    // ====================================================
    // FRONTEND APPLICATION DTO
    // ====================================================

    public static class FrontendApplicationDTO {

        private String email;
        private InstrumentDTO instrument;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public InstrumentDTO getInstrument() {
            return instrument;
        }

        public void setInstrument(
                InstrumentDTO instrument) {

            this.instrument = instrument;
        }
    }

    // ====================================================
    // INSTRUMENT DTO
    // ====================================================

    public static class InstrumentDTO {

        private String type;
        private String manufacturer;
        private String serialNumber;
        private String capacity;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getManufacturer() {
            return manufacturer;
        }

        public void setManufacturer(
                String manufacturer) {

            this.manufacturer = manufacturer;
        }

        public String getSerialNumber() {
            return serialNumber;
        }

        public void setSerialNumber(
                String serialNumber) {

            this.serialNumber = serialNumber;
        }

        public String getCapacity() {
            return capacity;
        }

        public void setCapacity(
                String capacity) {

            this.capacity = capacity;
        }
    }
}
