package com.example.legal_meterology.service;

import com.example.legal_meterology.entity.UserProfile;
import com.example.legal_meterology.repository.UserProfileRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserProfileRepository userProfileRepository;

    // This connects our bridge to the repository you just updated!
    public CustomUserDetailsService(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 1. Search the database for the user's email
        UserProfile user = userProfileRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // 2. If found, convert it into a Spring Security User object
        return User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole()) // e.g., "CUSTOMER", "LMO", or "ADMIN"
                .build();
    }
}
