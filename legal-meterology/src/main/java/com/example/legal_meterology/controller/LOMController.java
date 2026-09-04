package com.example.legal_meterology.controller;

import com.example.legal_meterology.entity.VerificationApplication;
import com.example.legal_meterology.service.LMOService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/lmo")
@CrossOrigin(origins = "*")
public class LMOController {

    private final LMOService lmoService;

    public LMOController(LMOService lmoService) {
        this.lmoService = lmoService;
    }

    // ----------------------------------------------------
    // 1. GET TASKS ASSIGNED TO LOGGED-IN LMO
    // ----------------------------------------------------

    @GetMapping("/tasks")
    public ResponseEntity<List<VerificationApplication>> getMyTasks(
            Principal principal) {

        String email = principal.getName();

        return ResponseEntity.ok(
                lmoService.getMyTasks(email)
        );
    }

    // ----------------------------------------------------
    // 2. GET ONE ASSIGNED TASK
    // ----------------------------------------------------

    @GetMapping("/tasks/{applicationId}")
    public ResponseEntity<?> getMyTask(
            @PathVariable UUID applicationId,
            Principal principal) {

        String email = principal.getName();

        VerificationApplication application =
                lmoService.getMyTask(applicationId, email);

        if (application == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(application);
    }

    // ----------------------------------------------------
    // 3. START VERIFICATION
    // ----------------------------------------------------

    @PostMapping("/tasks/{applicationId}/start")
    public ResponseEntity<String> startVerification(
            @PathVariable UUID applicationId,
            Principal principal) {

        String email = principal.getName();

        String result =
                lmoService.startVerification(
                        applicationId,
                        email
                );

        if (!"Verification started successfully".equals(result)) {
            return ResponseEntity.badRequest().body(result);
        }

        return ResponseEntity.ok(result);
    }

    // ----------------------------------------------------
    // 4. SUBMIT VERIFICATION MEASUREMENT
    // ----------------------------------------------------

    @PostMapping("/tasks/{applicationId}/verify")
    public ResponseEntity<String> verifyApplication(
            @PathVariable UUID applicationId,
            @RequestBody VerificationRequest request,
            Principal principal) {

        String email = principal.getName();

        String result =
                lmoService.verifyApplication(
                        applicationId,
                        request.getObservedValue(),
                        request.getRemarks(),
                        email
                );

        if ("PASS".equals(result) ||
                "FAIL".equals(result)) {

            return ResponseEntity.ok(result);
        }

        return ResponseEntity.badRequest().body(result);
    }

    // ----------------------------------------------------
    // REQUEST BODY FOR VERIFICATION
    // ----------------------------------------------------

    public static class VerificationRequest {

        private double observedValue;
        private String remarks;

        public double getObservedValue() {
            return observedValue;
        }

        public void setObservedValue(double observedValue) {
            this.observedValue = observedValue;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }
    }
}
