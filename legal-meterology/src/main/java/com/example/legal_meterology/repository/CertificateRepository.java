package com.example.legal_meterology.repository;

import com.example.legal_meterology.entity.Certificate;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CertificateRepository
        extends JpaRepository<Certificate, UUID> {
}
