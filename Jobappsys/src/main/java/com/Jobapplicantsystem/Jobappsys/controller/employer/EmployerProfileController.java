package com.Jobapplicantsystem.Jobappsys.controller.employer;

import com.Jobapplicantsystem.Jobappsys.model.Employer;
import com.Jobapplicantsystem.Jobappsys.repository.EmployerRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employer/profile")
@RequiredArgsConstructor
public class EmployerProfileController {

    private final EmployerRepository employerRepository;

    private String getEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @GetMapping
    public ResponseEntity<Employer> getProfile() {
        Employer emp = employerRepository.findByEmail(getEmail())
                .orElseThrow(() -> new RuntimeException("Employer not found"));

        return ResponseEntity.ok(emp);
    }

    @PutMapping
    public ResponseEntity<Employer> updateProfile(@RequestBody Employer data) {
        Employer emp = employerRepository.findByEmail(getEmail())
                .orElseThrow(() -> new RuntimeException("Employer not found"));

        emp.setCompanyName(data.getCompanyName());

        return ResponseEntity.ok(employerRepository.save(emp));
    }
}
