package com.example.legal_meterology.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "instruments")
public class Instrument {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String manufacturer;

    @Column(nullable = false, unique = true)
    private String serialNumber;

    @Column(nullable = false)
    private String capacityRange;

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public String getManufacturer() { return manufacturer; }
    public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }
    
    public String getSerialNumber() { return serialNumber; }
    public void setSerialNumber(String serialNumber) { this.serialNumber = serialNumber; }
    
    public String getCapacityRange() { return capacityRange; }
    public void setCapacityRange(String capacityRange) { this.capacityRange = capacityRange; }
}