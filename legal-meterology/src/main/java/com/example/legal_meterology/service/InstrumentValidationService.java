package com.example.legal_meterology.service;

import org.springframework.stereotype.Service;

@Service
public class InstrumentValidationService {

    public double getPermissibleLimit(String instrumentType) {

        if (instrumentType == null || instrumentType.trim().isEmpty()) {
            return 1.0;
        }

        String type = instrumentType.toLowerCase().trim();

        // Weighing instruments
        if (type.contains("weighing")
                || type.contains("weighing machine")
                || type.contains("scale")
                || type.contains("balance")) {

            return 0.5;
        }

        // Length measuring instruments
        if (type.contains("measuring")
                || type.contains("length")
                || type.contains("ruler")
                || type.contains("tape")) {

            return 1.0;
        }

        // Fuel measuring instruments
        if (type.contains("fuel")
                || type.contains("dispenser")
                || type.contains("petrol")) {

            return 0.1;
        }

        // Flow measuring instruments
        if (type.contains("water")
                || type.contains("flow")
                || type.contains("meter")) {

            return 1.0;
        }

        // Default prototype tolerance
        return 1.0;
    }
}
