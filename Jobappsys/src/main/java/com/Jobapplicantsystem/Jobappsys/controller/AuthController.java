package com.Jobapplicantsystem.Jobappsys.controller;

import com.Jobapplicantsystem.dto.request.LoginRequest;
import com.Jobapplicantsystem.dto.request.RegisterRequest;
import com.Jobapplicantsystem.dto.response.AuthResponse;
import com.Jobapplicantsystem.service.auth.AuthService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
