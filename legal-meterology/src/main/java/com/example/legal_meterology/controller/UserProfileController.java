package com.example.legal_meterology.controller;

import com.example.legal_meterology.entity.UserProfile;
import com.example.legal_meterology.repository.UserProfileRepository;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/profiles")
@CrossOrigin(origins = "*")
public class UserProfileController {

    private final UserProfileRepository repository;

    public UserProfileController(UserProfileRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public UserProfile createProfile(@RequestBody UserProfile profile) {
        return repository.save(profile);
    }

    @GetMapping
    public List<UserProfile> getAllProfiles() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public UserProfile getProfile(@PathVariable UUID id) {
        return repository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public UserProfile updateProfile(
            @PathVariable UUID id,
            @RequestBody UserProfile profile) {

        UserProfile existing = repository.findById(id).orElse(null);

        if (existing == null) {
            return null;
        }

        existing.setName(profile.getName());
        existing.setEmail(profile.getEmail());
        existing.setPhone(profile.getPhone());
        existing.setPassword(profile.getPassword());
        existing.setBusinessName(profile.getBusinessName());
        existing.setAddress(profile.getAddress());
        existing.setRole(profile.getRole());

        return repository.save(existing);
    }

    @DeleteMapping("/{id}")
    public String deleteProfile(@PathVariable UUID id) {

        if (!repository.existsById(id)) {
            return "Profile not found";
        }

        repository.deleteById(id);

        return "Profile deleted successfully";
    }
}
