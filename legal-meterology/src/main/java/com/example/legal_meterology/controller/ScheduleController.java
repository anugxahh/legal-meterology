package com.example.legal_meterology.controller;

import com.example.legal_meterology.entity.Schedule;
import com.example.legal_meterology.repository.ScheduleRepository;
import com.example.legal_meterology.service.VerificationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {

    private final ScheduleRepository repository;
    private final VerificationService verificationService;

    public ScheduleController(
            ScheduleRepository repository,
            VerificationService verificationService) {
        this.repository = repository;
        this.verificationService = verificationService;
    }

    @PostMapping
    public Schedule createSchedule(@RequestBody Schedule schedule) {
        return repository.save(schedule);
    }

    @GetMapping
    public List<Schedule> getAllSchedules() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Schedule getSchedule(@PathVariable Long id) {
        return repository.findById(id).orElse(null);
    }

    @DeleteMapping("/{id}")
    public String deleteSchedule(@PathVariable Long id) {
        repository.deleteById(id);
        return "Schedule deleted successfully";
    }

    @GetMapping("/check")
public String checkResult(
        @RequestParam("measuredValue") double measuredValue,
        @RequestParam("permissibleLimit") double permissibleLimit) {

    return verificationService.checkResult(
            measuredValue,
            permissibleLimit);
}
}
}
