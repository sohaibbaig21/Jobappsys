package com.Jobapplicantsystem.Jobappsys.service.auth;

import com.Jobapplicantsystem.Jobappsys.dto.request.LoginRequest;
import com.Jobapplicantsystem.Jobappsys.dto.request.RegisterRequest;
import com.Jobapplicantsystem.Jobappsys.dto.response.AuthResponse;
import com.Jobapplicantsystem.Jobappsys.model.Role;
import com.Jobapplicantsystem.Jobappsys.model.User;
import com.Jobapplicantsystem.Jobappsys.repository.UserRepository;
import com.Jobapplicantsystem.Jobappsys.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        System.out.println("➡️ STEP 1: Registration Request Received for: " + request.getEmail());

        if (userRepository.existsByEmail(request.getEmail())) {
            System.out.println("❌ ERROR: Email already exists.");
            throw new RuntimeException("Email already taken");
        }

        System.out.println("➡️ STEP 2: Creating User Object...");

        Role userRole = Role.APPLICANT;
        if (request.getRole() != null) {
            try {
                userRole = Role.valueOf(request.getRole().toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("❌ ERROR: Invalid role provided: " + request.getRole());
                throw new IllegalArgumentException("Invalid role: " + request.getRole());
            }
        }

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(userRole)
                .build();

        System.out.println("➡️ STEP 3: Saving to Repository...");
        try {
            userRepository.save(user);
            System.out.println("✅ STEP 4: Saved to Database! ID: " + user.getId());
        } catch (Exception e) {
            System.out.println("❌ FATAL ERROR Saving to DB: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Database Save Failed: " + e.getMessage());
        }

        System.out.println("➡️ STEP 5: Generating Token...");
        String token = jwtService.generateToken(user);
        System.out.println("✅ STEP 6: Token Generated. Registration Complete.");

        return AuthResponse.builder()
                .token(token)
                .email(user.getEmail())
                .role(user.getRole().name())
                .name(user.getFirstName())
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        System.out.println("➡️ LOGIN ATTEMPT: " + request.getEmail());

        // 1. Check if user exists explicitly to give better error message
        var userOptional = userRepository.findByEmail(request.getEmail());
        if (userOptional.isEmpty()) {
            System.out.println("❌ LOGIN FAILED: User email not found in DB.");
            throw new RuntimeException("User not found");
        }

        // 2. Attempt Authentication
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (Exception e) {
            System.out.println("❌ LOGIN FAILED: Bad Credentials (Password wrong).");
            throw new RuntimeException("Invalid password");
        }

        User user = userOptional.get();
        String token = jwtService.generateToken(user);

        System.out.println("✅ LOGIN SUCCESS!");
        return AuthResponse.builder()
                .token(token)
                .email(user.getEmail())
                .role(user.getRole().name())
                .name(user.getFirstName())
                .build();
    }
}