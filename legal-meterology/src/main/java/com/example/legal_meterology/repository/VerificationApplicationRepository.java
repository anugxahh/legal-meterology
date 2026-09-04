package com.example.legal_meterology.repository;

import com.example.legal_meterology.entity.UserProfile;
import com.example.legal_meterology.entity.VerificationApplication;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VerificationApplicationRepository
        extends JpaRepository<VerificationApplication, UUID> {

    // ----------------------------------------------------
    // 1. GET PENDING APPLICATIONS
    // Used by Admin to see applications waiting for assignment
    // ----------------------------------------------------
    List<VerificationApplication> findByStatusOrderBySubmittedAtAsc(
            String status);


    // ----------------------------------------------------
    // 2. GET APPLICATIONS ASSIGNED TO AN LMO
    // Used by LMO to see their assigned tasks
    // ----------------------------------------------------
    List<VerificationApplication> findByAssignedOfficerOrderBySubmittedAtAsc(
            UserProfile assignedOfficer);


    // ----------------------------------------------------
    // 3. GET APPLICATIONS OF A CUSTOMER
    // Used by Customer to see their applications
    // ----------------------------------------------------
    List<VerificationApplication> findByOwnerOrderBySubmittedAtDesc(
            UserProfile owner);


    // ----------------------------------------------------
    // 4. FIND A SPECIFIC APPLICATION ASSIGNED TO AN LMO
    // Important for security:
    // LMO can only access their own assigned application
    // ----------------------------------------------------
    Optional<VerificationApplication> findByIdAndAssignedOfficer(
            UUID applicationId,
            UserProfile assignedOfficer);
}
