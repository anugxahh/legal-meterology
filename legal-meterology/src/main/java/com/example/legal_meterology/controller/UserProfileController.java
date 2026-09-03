package com.example.legal_meterology.controller;

import com.example.legal_meterology.entity.UserProfile;
import com.example.legal_meterology.repository.UserProfileRepository;
import com.example.legal_meterology.service.JwtService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserProfileController {

    private final UserProfileRepository userProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public UserProfileController(
            UserProfileRepository userProfileRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService) {

        this.userProfileRepository = userProfileRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserProfile user) {

        if (userProfileRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.badRequest()
                    .body("Error: Email is already in use!");
        }

        // Encrypt password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Public registration creates CUSTOMER accounts
        user.setRole("CUSTOMER");

        userProfileRepository.save(user);

        return ResponseEntity.ok(
                "Success: User registered successfully!"
        );
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(
            @RequestBody UserProfile loginRequest) {

        try {

            // Authenticate email and password using Spring Security
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );

            // Get authenticated user
            UserProfile user = userProfileRepository
                    .findByEmail(loginRequest.getEmail())
                    .orElseThrow();

            // Create JWT
            String token = jwtService.generateToken(
                    org.springframework.security.core.userdetails.User
                            .withUsername(user.getEmail())
                            .password(user.getPassword())
                            .roles(user.getRole())
                            .build()
            );

            // Return JWT and basic user information
            return ResponseEntity.ok(
                    Map.of(
                            "token", token,
                            "email", user.getEmail(),
                            "name", user.getName(),
                            "role", user.getRole()
                    )
            );

        } catch (Exception e) {

            return ResponseEntity
                    .status(401)
                    .body("Error: Invalid email or password");
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
