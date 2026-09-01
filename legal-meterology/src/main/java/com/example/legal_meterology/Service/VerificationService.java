package com.example.legal_meterology.Service;

import org.springframework.stereotype.Service;

@Service
public class VerificationService {

    public String checkResult(double measuredValue, double permissibleLimit) {

        if (Math.abs(measuredValue) <= permissibleLimit) {
            return "PASS";
        } else {
            return "FAIL";
        }
    }
}
