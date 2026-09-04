package com.example.legal_meterology.service;

import org.springframework.stereotype.Service;

@Service
public class InstrumentValidationService {

    public double getPermissibleLimit(String instrumentType) {

        if (instrumentType == null) {
            return 1.0;
        }

        String type = instrumentType.toLowerCase();

        if (type.contains("weighing")
                || type.contains("weigh"))
            return 0.5;

        if (type.contains("measuring")
                || type.contains("length"))
            return 1.0;

        if (type.contains("fuel")
                || type.contains("dispenser"))
            return 0.1;

        if (type.contains("water")
                || type.contains("flow"))
            return 1.0;

        // Default prototype tolerance
        return 1.0;
    }
}
