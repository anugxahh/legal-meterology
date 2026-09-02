package com.example.legal_meterology.controller;
import java.util.UUID;

import org.springframework.web.bind.annotation.*;

import com.example.legal_meterology.service.VerificationService;

@RestController
@RequestMapping("/api/verification")
@CrossOrigin(origins = "*")
public class VerificationController {

    private final VerificationService verificationService;

    public VerificationController(
            VerificationService verificationService) {

        this.verificationService = verificationService;
    }

    @PostMapping("/{applicationId}")
    public String verifyApplication(
            @PathVariable UUID applicationId,
            @RequestParam double observedValue) {

        return verificationService.verifyApplication(
                applicationId,
                observedValue
        );
    }
}
