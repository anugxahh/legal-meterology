package com.example.legal_meterology.repository;

import com.example.legal_meterology.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.Optional; // We need to add this!

public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {
    
    // This is the magic line that lets Spring automatically write the SQL to search by email
    Optional<UserProfile> findByEmail(String email);
    
}