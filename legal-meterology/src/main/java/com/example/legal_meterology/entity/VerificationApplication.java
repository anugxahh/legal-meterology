package com.example.legal_meterology.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "verification_applications")
public class VerificationApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // Customer who owns the application
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private UserProfile owner;

    // Instrument being verified
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instrument_id", nullable = false)
    private Instrument instrument;

    // LMO assigned by Admin
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_officer_id")
    private UserProfile assignedOfficer;

    // PENDING -> ASSIGNED -> IN_PROGRESS -> VERIFIED
    @Column(nullable = false)
    private String status = "PENDING";

    // Date and time when application was submitted
    @Column(nullable = false)
    private LocalDateTime submittedAt;

    // Measurement recorded by LMO
    private Double observedValue;

    // System-generated result: PASS / FAIL
    private String result;

    // LMO/Admin remarks
    private String remarks;


    // Constructor
    public VerificationApplication() {
        this.submittedAt = LocalDateTime.now();
    }


    // Getters and Setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }


    public UserProfile getOwner() {
        return owner;
    }

    public void setOwner(UserProfile owner) {
        this.owner = owner;
    }


    public Instrument getInstrument() {
        return instrument;
    }

    public void setInstrument(Instrument instrument) {
        this.instrument = instrument;
    }


    public UserProfile getAssignedOfficer() {
        return assignedOfficer;
    }

    public void setAssignedOfficer(UserProfile assignedOfficer) {
        this.assignedOfficer = assignedOfficer;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }


    public Double getObservedValue() {
        return observedValue;
    }

    public void setObservedValue(Double observedValue) {
        this.observedValue = observedValue;
    }


    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }


    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
