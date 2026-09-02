package com.example.legal_meterology.repository;

import com.example.legal_meterology.entity.Instrument;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InstrumentRepository extends JpaRepository<Instrument, UUID> {
}
