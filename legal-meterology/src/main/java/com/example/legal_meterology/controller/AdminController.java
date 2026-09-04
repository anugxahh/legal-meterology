package com.example.legal_meterology.controller;

import com.example.legal_meterology.entity.UserProfile;
import com.example.legal_meterology.entity.VerificationApplication;
import com.example.legal_meterology.service.AdminService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // ----------------------------------------------------
    // 1. GET PENDING APPLICATIONS
    // ----------------------------------------------------

    @GetMapping("/applications")
    public ResponseEntity<List<VerificationApplication>>
    getPendingApplications() {

        return ResponseEntity.ok(
                adminService.getPendingApplications()
        );
    }

    // ----------------------------------------------------
    // 2. GET ALL LMOs
    // ----------------------------------------------------

    @GetMapping("/officers")
    public ResponseEntity<List<UserProfile>>
    getAllLMOs() {

        return ResponseEntity.ok(
                adminService.getAllLMOs()
        );
    }

    // ----------------------------------------------------
    // 3. ASSIGN APPLICATION TO LMO
    // ----------------------------------------------------

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

    // ----------------------------------------------------
    // 4. GET ONE APPLICATION
    // ----------------------------------------------------

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

    // ----------------------------------------------------
    // REQUEST BODY FOR ASSIGNMENT
    // ----------------------------------------------------

    public static class AssignOfficerRequest {

        private UUID officerId;

        public UUID getOfficerId() {
            return officerId;
        }

        public void setOfficerId(UUID officerId) {
            this.officerId = officerId;
        }
    }
}
