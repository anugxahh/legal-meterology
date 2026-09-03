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

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/customer")
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
            Optional<UserProfile> userOpt = userRepository.findByEmail(payload.getEmail());
            if (userOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Error: User account not found!");
            }
            UserProfile owner = userOpt.get();

            Instrument instrument = new Instrument();
            if (payload.getInstrument() != null) {
                instrument.setType(payload.getInstrument().getType() != null ? payload.getInstrument().getType() : "Not Specified");
                instrument.setManufacturer(payload.getInstrument().getManufacturer() != null ? payload.getInstrument().getManufacturer() : "Not Specified");
                
                String serial = payload.getInstrument().getSerialNumber();
                instrument.setSerialNumber((serial != null && !serial.trim().isEmpty()) ? serial.trim() : UUID.randomUUID().toString());
                
                instrument.setCapacityRange(payload.getInstrument().getCapacity() != null ? payload.getInstrument().getCapacity() : "Not Specified");
                instrument.setPermissibleLimit(0.0);
            } else {
                instrument.setType("Unknown");
                instrument.setManufacturer("Unknown");
                instrument.setSerialNumber(UUID.randomUUID().toString());
                instrument.setCapacityRange("Unknown");
                instrument.setPermissibleLimit(0.0);
            }
            
            instrument = instrumentRepository.save(instrument);

            VerificationApplication application = new VerificationApplication();
            application.setOwner(owner);
            application.setInstrument(instrument);
            application.setStatus("PENDING");

            applicationRepository.save(application);

            return ResponseEntity.ok("Success: Application submitted successfully!");

        } catch (DataIntegrityViolationException e) {
            System.err.println("DATABASE ERROR: Duplicate Serial Number or Missing Data.");
            return ResponseEntity.badRequest().body("Error: This Serial Number is already registered in the system.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error: An unexpected server error occurred.");
        }
    }

    // 2. Load Arjun's Dashboard Stats (SECURED WITH JWT)
    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Long>> getDashboardStats(Principal principal) {
        String loggedInEmail = principal.getName(); 

        List<VerificationApplication> myApps = applicationRepository.findAll().stream()
                .filter(a -> a.getOwner() != null && loggedInEmail.equalsIgnoreCase(a.getOwner().getEmail()))
                .collect(Collectors.toList());
        
        long pending = myApps.stream().filter(a -> "PENDING".equalsIgnoreCase(a.getStatus())).count();
        long accepted = myApps.stream().filter(a -> "ACCEPTED".equalsIgnoreCase(a.getStatus())).count();
        long rejected = myApps.stream().filter(a -> "REJECTED".equalsIgnoreCase(a.getStatus())).count();

        Map<String, Long> stats = new HashMap<>();
        stats.put("totalApplications", (long) myApps.size());
        stats.put("pending", pending);
        stats.put("accepted", accepted);
        stats.put("rejected", rejected);

        return ResponseEntity.ok(stats);
    }

    // 3. Preserved Hari's Original Endpoints (SECURED WITH JWT)
    @GetMapping("/applications")
    public List<VerificationApplication> getAllApplications(Principal principal) {
        String loggedInEmail = principal.getName();
        
        return applicationRepository.findAll().stream()
                .filter(a -> a.getOwner() != null && loggedInEmail.equalsIgnoreCase(a.getOwner().getEmail()))
                .collect(Collectors.toList());
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
    // DTO CLASSES
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