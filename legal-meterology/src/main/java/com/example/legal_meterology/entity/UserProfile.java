package com.example.legal_meterology.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "profiles")
public class UserProfile {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String contactEmail;

    @Column(nullable = false)
    private String role; 

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    public String getContactEmail() { return contactEmail; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}