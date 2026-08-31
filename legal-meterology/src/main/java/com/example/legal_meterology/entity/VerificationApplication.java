package com.example.legal_meterology.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "verification_applications")
public class VerificationApplication {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // This creates a Foreign Key linking to the UserProfile table
    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private UserProfile owner;

    // This creates a Foreign Key linking to the Instrument table
    @ManyToOne
    @JoinColumn(name = "instrument_id", nullable = false)
    private Instrument instrument;

    @Column(nullable = false)
    private String status = "PENDING"; // PENDING, SCHEDULED, VERIFIED

    @Column(nullable = true)
    private Double observedValue; // The weight the LMO types in

    @Column(nullable = true)
    private String result; // PASS or FAIL

    @Column(nullable = true)
    private String remarks; // Notes from the LMO

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UserProfile getOwner() { return owner; }
    public void setOwner(UserProfile owner) { this.owner = owner; }

    public Instrument getInstrument() { return instrument; }
    public void setInstrument(Instrument instrument) { this.instrument = instrument; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Double getObservedValue() { return observedValue; }
    public void setObservedValue(Double observedValue) { this.observedValue = observedValue; }

    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
}