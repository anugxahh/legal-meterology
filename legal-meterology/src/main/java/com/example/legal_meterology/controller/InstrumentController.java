package com.example.legal_meterology.controller;

import com.example.legal_meterology.entity.Instrument;
import com.example.legal_meterology.repository.InstrumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/instruments")
public class InstrumentController {

    @Autowired
    private InstrumentRepository instrumentRepository;

    // POST: Adds a new instrument to the database
    @PostMapping
    public Instrument addInstrument(@RequestBody Instrument instrument) {
        return instrumentRepository.save(instrument);
    }

    // GET: Fetches all instruments from the database
    @GetMapping
    public List<Instrument> getAllInstruments() {
        return instrumentRepository.findAll();
    }
}