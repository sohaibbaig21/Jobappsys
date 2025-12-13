package com.Jobapplicantsystem.Jobappsys.controller.applicant;

import com.Jobapplicantsystem.Jobappsys.model.SavedJob;
import com.Jobapplicantsystem.Jobappsys.service.applicant.ApplicantService; // <--- THIS IMPORT WAS MISSING
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/applicant/saved-jobs")
@RequiredArgsConstructor
public class SavedJobsController {

    private final ApplicantService applicantService;

    private String getEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @PostMapping("/{jobId}")
    public ResponseEntity<?> saveJob(@PathVariable Long jobId) {
        SavedJob saved = applicantService.saveJob(getEmail(), jobId);
        return ResponseEntity.ok(Map.of("message", "Job saved successfully", "id", saved.getSavedJobId()));
    }

    @GetMapping
    public ResponseEntity<List<SavedJob>> getSavedJobs() {
        return ResponseEntity.ok(applicantService.getSavedJobs(getEmail()));
    }
}