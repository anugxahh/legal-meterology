package com.example.legal_meterology.repository;

import com.example.legal_meterology.entity.VerificationApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VerificationApplicationRepository
        extends JpaRepository<VerificationApplication, UUID> {
}
