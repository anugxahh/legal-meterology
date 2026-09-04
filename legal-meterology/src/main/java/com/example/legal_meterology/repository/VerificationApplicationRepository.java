package com.example.legal_meterology.repository;

import com.example.legal_meterology.entity.VerificationApplication;
import com.example.legal_meterology.entity.UserProfile;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface VerificationApplicationRepository
        extends JpaRepository<VerificationApplication, UUID> {

    // All applications waiting for an LMO
    List<VerificationApplication> findByStatusOrderBySubmittedAtAsc(String status);

    // Applications assigned to a particular LMO
    List<VerificationApplication> findByAssignedOfficerOrderBySubmittedAtAsc(
            UserProfile assignedOfficer);

    // Applications belonging to a particular customer
    List<VerificationApplication> findByOwnerOrderBySubmittedAtDesc(
            UserProfile owner);
}
