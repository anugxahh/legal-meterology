package com.example.legal_meterology.controller;

import com.example.legal_meterology.entity.Instrument;
import com.example.legal_meterology.repository.InstrumentRepository;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/instruments")
@CrossOrigin(origins = "*")
public class InstrumentController {

    private final InstrumentRepository instrumentRepository;

    public InstrumentController(InstrumentRepository instrumentRepository) {
        this.instrumentRepository = instrumentRepository;
    }

    @PostMapping
    public Instrument addInstrument(@RequestBody Instrument instrument) {
        return instrumentRepository.save(instrument);
    }

    @GetMapping
    public List<Instrument> getAllInstruments() {
        return instrumentRepository.findAll();
    }

    @GetMapping("/{id}")
    public Instrument getInstrument(@PathVariable UUID id) {
        return instrumentRepository.findById(id).orElse(null);
    }
}
