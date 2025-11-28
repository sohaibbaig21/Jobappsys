package com.Jobapplicantsystem.Jobappsys.controller.employer;

import com.Jobapplicantsystem.model.JobApplication;
import com.Jobapplicantsystem.service.employer.ApplicationReviewService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employer/applications")
@RequiredArgsConstructor
public class ApplicationManagementController {

    private final ApplicationReviewService reviewService;

    private String getEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @GetMapping("/{jobId}")
    public ResponseEntity<List<JobApplication>> getApplications(@PathVariable Long jobId) {
        return ResponseEntity.ok(reviewService.getJobApplications(getEmail(), jobId));
    }

    @PutMapping("/{applicationId}/status")
    public ResponseEntity<JobApplication> updateStatus(
            @PathVariable Long applicationId,
            @RequestParam String status
    ) {
        return ResponseEntity.ok(reviewService.updateStatus(getEmail(), applicationId, status));
    }
}
