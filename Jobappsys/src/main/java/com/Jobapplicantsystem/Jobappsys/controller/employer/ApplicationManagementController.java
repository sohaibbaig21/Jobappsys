package com.Jobapplicantsystem.Jobappsys.controller.employer;

import com.Jobapplicantsystem.Jobappsys.dto.response.ApplicationResponse;
import com.Jobapplicantsystem.Jobappsys.service.employer.ApplicationReviewService;

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

    @GetMapping("/{jobPostId}")
    public ResponseEntity<List<ApplicationResponse>> getApplicationsByJobPost(@PathVariable Long jobPostId) {
        String employerEmail = getEmail();
        return ResponseEntity.ok(reviewService.getApplicationsByJobPost(jobPostId, employerEmail));
    }

    @PutMapping("/{applicationId}/accept")
    public ResponseEntity<Void> acceptApplication(@PathVariable Long applicationId) {
        reviewService.acceptApplication(applicationId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{applicationId}/reject")
    public ResponseEntity<Void> rejectApplication(@PathVariable Long applicationId) {
        reviewService.rejectApplication(applicationId);
        return ResponseEntity.ok().build();
    }
}
