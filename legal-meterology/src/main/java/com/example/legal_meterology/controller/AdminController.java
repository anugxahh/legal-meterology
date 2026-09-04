package com.example.legal_meterology.controller;

import com.example.legal_meterology.entity.UserProfile;
import com.example.legal_meterology.entity.VerificationApplication;
import com.example.legal_meterology.service.AdminService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/applications")
    public ResponseEntity<List<VerificationApplication>> getPendingApplications() {
        return ResponseEntity.ok(adminService.getPendingApplications());
    }

    @GetMapping("/officers")
    public ResponseEntity<List<UserProfile>> getAllLMOs() {
        return ResponseEntity.ok(adminService.getAllLMOs());
    }

    // Existing endpoint
    @PostMapping("/applications/{applicationId}/assign")
    public ResponseEntity<String> assignApplication(
            @PathVariable UUID applicationId,
            @RequestBody AssignOfficerRequest request) {

        String result = adminService.assignApplicationToLMO(
                applicationId,
                request.getOfficerId()
        );

        if (!"Application assigned successfully".equals(result)) {
            return ResponseEntity.badRequest().body(result);
        }

        return ResponseEntity.ok(result);
    }

    // Compatibility endpoint used by the current frontend
    @PostMapping("/assign")
    public ResponseEntity<String> assignApplicationLegacy(
            @RequestBody AssignOfficerRequest request) {

        if (request.getApplicationId() == null ||
            request.getOfficerId() == null) {

            return ResponseEntity.badRequest()
                    .body("applicationId and officerId are required");
        }

        String result = adminService.assignApplicationToLMO(
                request.getApplicationId(),
                request.getOfficerId()
        );

        if (!"Application assigned successfully".equals(result)) {
            return ResponseEntity.badRequest().body(result);
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping("/applications/{applicationId}")
    public ResponseEntity<?> getApplication(
            @PathVariable UUID applicationId) {

        VerificationApplication application =
                adminService.getApplication(applicationId);

        if (application == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(application);
    }

    public static class AssignOfficerRequest {

        private UUID applicationId;
        private UUID officerId;

        public UUID getApplicationId() {
            return applicationId;
        }

        public void setApplicationId(UUID applicationId) {
            this.applicationId = applicationId;
        }

        public UUID getOfficerId() {
            return officerId;
        }

        public void setOfficerId(UUID officerId) {
            this.officerId = officerId;
        }
    }
}
