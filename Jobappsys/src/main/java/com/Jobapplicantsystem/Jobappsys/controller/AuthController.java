package com.Jobapplicantsystem.Jobappsys.controller;

import com.Jobapplicantsystem.Jobappsys.dto.request.LoginRequest;
import com.Jobapplicantsystem.Jobappsys.dto.request.RegisterRequest;
import com.Jobapplicantsystem.Jobappsys.service.auth.AuthService;
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
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            return ResponseEntity.ok(authService.register(request));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage()); // 400 Bad Request for invalid role
        } catch (RuntimeException e) {
            if ("Email already taken".equals(e.getMessage())) {
                return ResponseEntity.status(409).body(e.getMessage()); // 409 Conflict
            } else if (e.getMessage() != null && e.getMessage().startsWith("Database Save Failed")) {
                return ResponseEntity.status(500).body("Registration failed due to a database error."); // 500 Internal Server Error
            }
            return ResponseEntity.badRequest().body(e.getMessage()); // Generic 400 for other RuntimeExceptions
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("An unexpected server error occurred during registration."); // 500 Internal Server Error
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            return ResponseEntity.ok(authService.login(request));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(401).body("Invalid email or password");
        }
    }
}