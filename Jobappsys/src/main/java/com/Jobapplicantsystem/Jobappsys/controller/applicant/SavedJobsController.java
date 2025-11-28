package com.Jobapplicantsystem.Jobappsys.controller.applicant;

import com.Jobapplicantsystem.model.SavedJob;
import com.Jobapplicantsystem.service.applicant.ApplicantService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applicant/saved-jobs")
@RequiredArgsConstructor
public class SavedJobsController {

    private final ApplicantService applicantService;

    private String getEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @PostMapping("/{jobId}")
    public ResponseEntity<SavedJob> saveJob(@PathVariable Long jobId) {
        return ResponseEntity.ok(applicantService.saveJob(getEmail(), jobId));
    }

    @GetMapping
    public ResponseEntity<List<SavedJob>> getSavedJobs() {
        return ResponseEntity.ok(applicantService.getSavedJobs(getEmail()));
    }
}
