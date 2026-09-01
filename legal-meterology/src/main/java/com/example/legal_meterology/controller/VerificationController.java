package com.example.legal_meterology.controller;

import com.example.legal_meterology.Service.VerificationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/verification")
public class VerificationController {

    private final VerificationService verificationService;

    public VerificationController(VerificationService verificationService) {
        this.verificationService = verificationService;
    }

    @GetMapping("/check")
    public String checkResult(
            @RequestParam("measuredValue") double measuredValue,
            @RequestParam("permissibleLimit") double permissibleLimit) {

        return verificationService.checkResult(
                measuredValue,
                permissibleLimit);
    }
}
