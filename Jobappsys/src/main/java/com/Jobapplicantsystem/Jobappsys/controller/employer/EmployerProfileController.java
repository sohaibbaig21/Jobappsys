package com.Jobapplicantsystem.Jobappsys.controller.employer;

import com.Jobapplicantsystem.Jobappsys.dto.request.EmployerProfileRequest;
import com.Jobapplicantsystem.Jobappsys.model.Employer;
import com.Jobapplicantsystem.Jobappsys.model.User;
import com.Jobapplicantsystem.Jobappsys.repository.EmployerRepository;
import com.Jobapplicantsystem.Jobappsys.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/employer/profile")
@RequiredArgsConstructor
public class EmployerProfileController {

    private final UserRepository userRepository;
    private final EmployerRepository employerRepository;

    private String getEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    // Derive a stable UUID for employers.user_id from our local user ID
    private UUID toUserUuid(User user) {
        String ns = "jobappsys:user:" + user.getId();
        return UUID.nameUUIDFromBytes(ns.getBytes());
    }

    @GetMapping
    public ResponseEntity<?> getProfile() {
        User user = userRepository.findByEmail(getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        UUID userUuid = toUserUuid(user);

        Optional<Employer> emp = employerRepository.findByUserId(userUuid);
        if (emp.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("message", "Employer profile not found"));
        }
        return ResponseEntity.ok(emp.get());
    }

    @PostMapping
    public ResponseEntity<?> createProfile(@RequestBody EmployerProfileRequest req) {
        if (req.getCompanyName() == null || req.getCompanyName().isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "companyName is required"));
        }

        User user = userRepository.findByEmail(getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        UUID userUuid = toUserUuid(user);

        Optional<Employer> existing = employerRepository.findByUserId(userUuid);
        if (existing.isPresent()) {
            return ResponseEntity.status(409).body(Map.of("error", "Employer profile already exists"));
        }

        Employer employer = Employer.builder()
                .userId(userUuid)
                .companyName(req.getCompanyName())
                .companyDescription(req.getCompanyDescription())
                .companyWebsite(req.getCompanyWebsite())
                .companySize(req.getCompanySize())
                .industry(req.getIndustry())
                .logoUrl(req.getLogoUrl())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        Employer saved = employerRepository.save(employer);
        return ResponseEntity.ok(saved);
    }

    @PutMapping
    public ResponseEntity<?> updateProfile(@RequestBody EmployerProfileRequest req) {
        if (req.getCompanyName() == null || req.getCompanyName().isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "companyName is required"));
        }

        User user = userRepository.findByEmail(getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        UUID userUuid = toUserUuid(user);

        Employer emp = employerRepository.findByUserId(userUuid)
                .orElse(Employer.builder()
                        .userId(userUuid)
                        .createdAt(Instant.now())
                        .build());

        emp.setCompanyName(req.getCompanyName());
        emp.setCompanyDescription(req.getCompanyDescription());
        emp.setCompanyWebsite(req.getCompanyWebsite());
        emp.setCompanySize(req.getCompanySize());
        emp.setIndustry(req.getIndustry());
        emp.setLogoUrl(req.getLogoUrl());
        emp.setUpdatedAt(Instant.now());

        Employer saved = employerRepository.save(emp);
        return ResponseEntity.ok(saved);
    }
}
