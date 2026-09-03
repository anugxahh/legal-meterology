package com.example.legal_meterology.controller;

import com.example.legal_meterology.entity.Instrument;
import com.example.legal_meterology.entity.UserProfile;
import com.example.legal_meterology.entity.VerificationApplication;
import com.example.legal_meterology.repository.InstrumentRepository;
import com.example.legal_meterology.repository.UserProfileRepository;
import com.example.legal_meterology.repository.VerificationApplicationRepository;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/customer") // <-- Matches Arjun's frontend JS route
@CrossOrigin(origins = "*")
public class ApplicationController {

    private final VerificationApplicationRepository applicationRepository;
    private final UserProfileRepository userRepository;
    private final InstrumentRepository instrumentRepository;

    public ApplicationController(
            VerificationApplicationRepository applicationRepository,
            UserProfileRepository userRepository,
            InstrumentRepository instrumentRepository) {
        this.applicationRepository = applicationRepository;
        this.userRepository = userRepository;
        this.instrumentRepository = instrumentRepository;
    }

    // 1. Catch Arjun's Form and Save to Hari's Database
    @PostMapping("/applications")
    public ResponseEntity<?> createApplication(@RequestBody FrontendApplicationDTO payload) {
        
        try {
            // Step A: Find the user in the database using the email from the form
            Optional<UserProfile> userOpt = userRepository.findByEmail(payload.getEmail());
            if (userOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Error: User account not found!");
            }
            UserProfile owner = userOpt.get();

            // Step B: Create and save the Instrument exactly as Hari designed it
            Instrument instrument = new Instrument();
            if (payload.getInstrument() != null) {
                instrument.setType(payload.getInstrument().getType() != null ? payload.getInstrument().getType() : "Not Specified");
                instrument.setManufacturer(payload.getInstrument().getManufacturer() != null ? payload.getInstrument().getManufacturer() : "Not Specified");
                
                // Hari made Serial Number unique and non-null. If the UI leaves it blank, we generate a random one to prevent DB crashes!
                String serial = payload.getInstrument().getSerialNumber();
                instrument.setSerialNumber((serial != null && !serial.isEmpty()) ? serial : UUID.randomUUID().toString());
                
                instrument.setCapacityRange(payload.getInstrument().getCapacity() != null ? payload.getInstrument().getCapacity() : "Not Specified");
                
                // The frontend UI doesn't collect permissible limit yet, so we default it to 0.0 to satisfy Hari's @Column(nullable = false)
                instrument.setPermissibleLimit(0.0);
            } else {
                // Failsafe if instrument data is missing entirely
                instrument.setType("Unknown");
                instrument.setManufacturer("Unknown");
                instrument.setSerialNumber(UUID.randomUUID().toString());
                instrument.setCapacityRange("Unknown");
                instrument.setPermissibleLimit(0.0);
            }
            
            // Save the instrument first so it has an ID to link to the application
            instrument = instrumentRepository.save(instrument);

            // Step C: Create the Application and link the Foreign Keys
            VerificationApplication application = new VerificationApplication();
            application.setOwner(owner);
            application.setInstrument(instrument);
            application.setStatus("PENDING");

            applicationRepository.save(application);

            return ResponseEntity.ok("Success: Application submitted successfully!");

        } catch (DataIntegrityViolationException e) {
            // This catches Hari's unique constraints (like duplicate serial numbers)
            System.err.println("DATABASE ERROR: " + e.getMostSpecificCause().getMessage());
            return ResponseEntity.badRequest().body("Error: This Serial Number is already registered in the system.");
        } catch (Exception e) {
            // This catches any other unexpected crashes
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error: An unexpected server error occurred.");
        }
    }

    // 2. Load Arjun's Dashboard Stats
    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Long>> getDashboardStats() {
        List<VerificationApplication> allApps = applicationRepository.findAll();
        
        long pending = allApps.stream().filter(a -> "PENDING".equalsIgnoreCase(a.getStatus())).count();
        long accepted = allApps.stream().filter(a -> "ACCEPTED".equalsIgnoreCase(a.getStatus())).count();
        long rejected = allApps.stream().filter(a -> "REJECTED".equalsIgnoreCase(a.getStatus())).count();

        Map<String, Long> stats = new HashMap<>();
        stats.put("totalApplications", (long) allApps.size());
        stats.put("pending", pending);
        stats.put("accepted", accepted);
        stats.put("rejected", rejected);

        return ResponseEntity.ok(stats);
    }

    // 3. Preserved Hari's Original Endpoints (Mapped to /applications)
    @GetMapping("/applications")
    public List<VerificationApplication> getAllApplications() {
        return applicationRepository.findAll();
    }

    @GetMapping("/applications/{id}")
    public VerificationApplication getApplication(@PathVariable UUID id) {
        return applicationRepository.findById(id).orElse(null);
    }

    @DeleteMapping("/applications/{id}")
    public String deleteApplication(@PathVariable UUID id) {
        if (!applicationRepository.existsById(id)) {
            return "Application not found";
        }
        applicationRepository.deleteById(id);
        return "Application deleted successfully";
    }

    // =====================================================================
    // DTO CLASSES: These map exactly to Arjun's JS object structure
    // =====================================================================
    public static class FrontendApplicationDTO {
        private String email;
        private InstrumentDTO instrument;
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public InstrumentDTO getInstrument() { return instrument; }
        public void setInstrument(InstrumentDTO instrument) { this.instrument = instrument; }
    }

    public static class InstrumentDTO {
        private String type;
        private String manufacturer;
        private String serialNumber;
        private String capacity;
        
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public String getManufacturer() { return manufacturer; }
        public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }
        public String getSerialNumber() { return serialNumber; }
        public void setSerialNumber(String serialNumber) { this.serialNumber = serialNumber; }
        public String getCapacity() { return capacity; }
        public void setCapacity(String capacity) { this.capacity = capacity; }
    }
}