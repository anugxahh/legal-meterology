package com.example.legal_meterology.entity;

import jakarta.persistence.*;
import java.util.UUID;
import java.time.LocalDate;

@Entity
@Table(name = "certificates")
public class Certificate {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id; // This ID is what the QR code will scan!

    @OneToOne
    @JoinColumn(name = "application_id", nullable = false)
    private VerificationApplication application;

    @Column(nullable = false)
    private LocalDate issueDate;

    @Column(nullable = false)
    private LocalDate validUntil;

    @Column(nullable = false)
    private String status = "VALID"; // VALID, EXPIRED, REVOKED

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    
    public VerificationApplication getApplication() { return application; }
    public void setApplication(VerificationApplication application) { this.application = application; }
    
    public LocalDate getIssueDate() { return issueDate; }
    public void setIssueDate(LocalDate issueDate) { this.issueDate = issueDate; }
    
    public LocalDate getValidUntil() { return validUntil; }
    public void setValidUntil(LocalDate validUntil) { this.validUntil = validUntil; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}