package com.example.legal_meterology.controller;

import com.example.legal_meterology.Service.VerificationService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/verification")
@CrossOrigin(origins = "*")
public class VerificationController {

    private final VerificationService verificationService;

    public VerificationController(VerificationService verificationService) {
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
