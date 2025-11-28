package com.Jobapplicantsystem.Jobappsys.controller.applicant;

import com.Jobapplicantsystem.model.Applicant;
import com.Jobapplicantsystem.service.applicant.ApplicantProfileService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/applicant/profile")
@RequiredArgsConstructor
public class ApplicantProfileController {

    private final ApplicantProfileService profileService;

    private String getEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @GetMapping
    public ResponseEntity<Applicant> getProfile() {
        return ResponseEntity.ok(profileService.getProfile(getEmail()));
    }

    @PutMapping
    public ResponseEntity<Applicant> updateProfile(@RequestBody Applicant data) {
        return ResponseEntity.ok(profileService.updateProfile(getEmail(), data));
    }

    @PostMapping("/upload-resume")
    public ResponseEntity<String> uploadResume(@RequestParam("file") MultipartFile file) throws Exception {
        return ResponseEntity.ok(profileService.uploadResume(getEmail(), file));
    }
}
