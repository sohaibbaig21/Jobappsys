package com.Jobapplicantsystem.Jobappsys.controller.employer;

import com.Jobapplicantsystem.model.Employer;
import com.Jobapplicantsystem.model.User;
import com.Jobapplicantsystem.repository.EmployerRepository;
import com.Jobapplicantsystem.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employer/profile")
@RequiredArgsConstructor
public class EmployerProfileController {

    private final UserRepository userRepository;
    private final EmployerRepository employerRepository;

    private String getEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @GetMapping
    public ResponseEntity<Employer> getProfile() {
        User user = userRepository.findByEmail(getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Employer emp = employerRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("Employer not found"));

        return ResponseEntity.ok(emp);
    }

    @PutMapping
    public ResponseEntity<Employer> updateProfile(@RequestBody Employer data) {
        User user = userRepository.findByEmail(getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Employer emp = employerRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("Employer not found"));

        emp.setCompanyName(data.getCompanyName());
        emp.setWebsite(data.getWebsite());
        emp.setLocation(data.getLocation());
        emp.setDescription(data.getDescription());

        return ResponseEntity.ok(employerRepository.save(emp));
    }
}
