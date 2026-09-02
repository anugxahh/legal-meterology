package com.example.legal_meterology.controller;

import com.example.legal_meterology.entity.UserProfile;
import com.example.legal_meterology.repository.UserProfileRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserProfileController {

    private final UserProfileRepository userProfileRepository;
    private final PasswordEncoder passwordEncoder;

    public UserProfileController(UserProfileRepository userProfileRepository, PasswordEncoder passwordEncoder) {
        this.userProfileRepository = userProfileRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserProfile user) {
        if (userProfileRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Error: Email is already in use!");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userProfileRepository.save(user);
        return ResponseEntity.ok("Success: User registered successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserProfile loginRequest) {
        var userOptional = userProfileRepository.findByEmail(loginRequest.getEmail());
        
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(401).body("Error: Invalid email or password");
        }

        UserProfile user = userOptional.get();

        if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.ok("Success: Login successful! Welcome " + user.getName());
        } else {
            return ResponseEntity.status(401).body("Error: Invalid email or password");
        }
    }

    @DeleteMapping("/{id}")
    public String deleteProfile(@PathVariable UUID id) {
        if (!userProfileRepository.existsById(id)) {
            return "Profile not found";
        }
        userProfileRepository.deleteById(id);
        return "Profile deleted successfully";
    }
}