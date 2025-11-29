package com.Jobapplicantsystem.Jobappsys.service.auth;

import com.Jobapplicantsystem.Jobappsys.dto.request.LoginRequest;
import com.Jobapplicantsystem.Jobappsys.dto.request.RegisterRequest;
import com.Jobapplicantsystem.Jobappsys.dto.response.AuthResponse;
import com.Jobapplicantsystem.Jobappsys.model.User;
import com.Jobapplicantsystem.Jobappsys.repository.UserRepository;

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
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    // REGISTER NEW USER
    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        User user = User.builder()
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .userType(parseUserType(request.getRole())) // APPLICANT or EMPLOYER
                .build();

        userRepository.save(user);

        String token = jwtService.generateToken(
                org.springframework.security.core.userdetails.User
                        .withUsername(user.getEmail())
                        .password(user.getPasswordHash())
                        .authorities(user.getUserType().toString())
                        .build()
        );

        AuthResponse resp = new AuthResponse();
        resp.setToken(token);
        resp.setEmail(user.getEmail());
        resp.setRole(user.getUserType().toString());
        return resp;
    }

    // LOGIN
    public AuthResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtService.generateToken(
                org.springframework.security.core.userdetails.User
                        .withUsername(user.getEmail())
                        .password(user.getPasswordHash())
                        .authorities(user.getUserType().toString())
                        .build()
        );

        AuthResponse resp = new AuthResponse();
        resp.setToken(token);
        resp.setEmail(user.getEmail());
        resp.setRole(user.getUserType().toString());
        return resp;
    }

    private User.UserType parseUserType(String role) {
        if (role == null) {
            return User.UserType.APPLICANT; // default role
        }
        try {
            return User.UserType.valueOf(role.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return User.UserType.APPLICANT;
        }
    }
}
