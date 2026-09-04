package com.example.legal_meterology.config;

import com.example.legal_meterology.entity.UserProfile;
import com.example.legal_meterology.repository.UserProfileRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initializeUsers(
            UserProfileRepository userProfileRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            // =========================
            // CREATE DEMO ADMIN
            // =========================

            String adminEmail = "admin@legalmetrology.com";

            if (userProfileRepository
                    .findByEmail(adminEmail)
                    .isEmpty()) {

                UserProfile admin = new UserProfile();

                admin.setName("System Administrator");
                admin.setEmail(adminEmail);
                admin.setPhone("9999999999");
                admin.setBusinessName("Legal Metrology Department");
                admin.setAddress("Government Office");
                admin.setRole("ADMIN");

                // Password is stored encrypted
                admin.setPassword(
                        passwordEncoder.encode("admin123")
                );

                userProfileRepository.save(admin);

                System.out.println(
                        "========================================"
                );
                System.out.println(
                        "DEMO ADMIN CREATED SUCCESSFULLY"
                );
                System.out.println(
                        "Email: admin@legalmetrology.com"
                );
                System.out.println(
                        "========================================"
                );

            } else {

                System.out.println(
                        "Demo Admin already exists."
                );
            }
        };
    }
}
