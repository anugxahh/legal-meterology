package com.example.legal_meterology.controller;

import com.example.legal_meterology.entity.Schedule;
import com.example.legal_meterology.repository.ScheduleRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {

    private final ScheduleRepository repository;

    public ScheduleController(ScheduleRepository repository) {
        this.repository = repository;
    }

    // CREATE
    @PostMapping
    public Schedule createSchedule(@RequestBody Schedule schedule) {
        return repository.save(schedule);
    }

    // GET ALL
    @GetMapping
    public List<Schedule> getAllSchedules() {
        return repository.findAll();
    }

    // GET BY ID
    @GetMapping("/{id}")
    public Schedule getSchedule(@PathVariable Long id) {
        return repository.findById(id).orElse(null);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public String deleteSchedule(@PathVariable Long id) {
        repository.deleteById(id);
        return "Schedule deleted successfully";
    }
}
