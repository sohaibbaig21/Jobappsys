package com.Jobapplicantsystem.Jobappsys.controller.applicant;

import com.Jobapplicantsystem.Jobappsys.dto.request.JobApplicationRequest;
import com.Jobapplicantsystem.Jobappsys.dto.response.JobApplicationResponse;
import com.Jobapplicantsystem.Jobappsys.service.applicant.ApplicationService;
import com.Jobapplicantsystem.Jobappsys.repository.UserRepository;
import com.Jobapplicantsystem.Jobappsys.repository.ApplicantRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.Jobapplicantsystem.Jobappsys.exception.ResourceNotFoundException;

//What does this file do?
// Apply for a job - Applicants submit applications with answers and resume
//View my applications - Get all applications submitted by the logged-in user
//View applications for a job - Employers see all applications for their job post
//Update application status - Change status (APPLIED → HIRED/REJECTED)
//Delete application - Remove an application



@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationController.class);
    private final ApplicationService applicationService;
    private final UserRepository userRepository;
    private final ApplicantRepository applicantRepository;

    @PostMapping("/apply/{jobPostId}")
public ResponseEntity<?> applyForJob(@PathVariable Long jobPostId,
                                                          @AuthenticationPrincipal UserDetails principal,
                                                          @RequestPart("applicationRequest") JobApplicationRequest applicationRequest,
                                                          @RequestPart(value = "resumeFile", required = false) MultipartFile resumeFile) {
    
    System.out.println("=============================================");
    System.out.println("🟢 STEP 1: Apply endpoint hit");
    System.out.println("Job Post ID: " + jobPostId);
    System.out.println("Current Principal: " + (principal != null ? principal.getUsername() : "NULL"));
    System.out.println("Application Request: " + applicationRequest);
    System.out.println("Resume File: " + (resumeFile != null ? resumeFile.getOriginalFilename() : "NULL"));
    System.out.println("=============================================");

    if (principal == null) {
        logger.error("Unauthorized: No current user found for job application.");
        return ResponseEntity.status(401).body(Map.of(
            "error", "Unauthorized",
            "message", "No authenticated user found. Please log in."
        ));
    }

    try {
        // Load domain user by principal username (email)
        var domainUser = userRepository.findByEmail(principal.getUsername())
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));

        logger.info("Received apply for job request for jobPostId: {} by user: {}", jobPostId, domainUser.getEmail());

        // Resolve Applicant by the authenticated user's ID
        var applicant = applicantRepository.findByUserId(domainUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Applicant profile not found for user: " + domainUser.getEmail()));

        // The apply method requires the Applicant ID, not the User ID
        JobApplicationResponse response = applicationService.applyForJob(jobPostId, applicant.getId(), applicationRequest, resumeFile);
        return ResponseEntity.ok(response);
        
    } catch (ResourceNotFoundException e) {
        System.out.println("=============================================");
        System.out.println("❌❌❌ RESOURCE NOT FOUND IN CONTROLLER ❌❌❌");
        System.out.println("Exception Type: " + e.getClass().getName());
        System.out.println("Exception Message: " + e.getMessage());
        e.printStackTrace();
        System.out.println("=============================================");
        return ResponseEntity.status(404).body(Map.of(
            "error", "Not Found",
            "message", e.getMessage()
        ));
    } catch (IllegalArgumentException e) {
        System.out.println("=============================================");
        System.out.println("❌❌❌ BAD REQUEST IN CONTROLLER ❌❌❌");
        System.out.println("Exception Type: " + e.getClass().getName());
        System.out.println("Exception Message: " + e.getMessage());
        e.printStackTrace();
        System.out.println("=============================================");
        return ResponseEntity.badRequest().body(Map.of(
            "error", "Bad Request",
            "message", e.getMessage()
        ));
    } catch (Exception e) {
        System.out.println("=============================================");
        System.out.println("❌❌❌ UNEXPECTED EXCEPTION CAUGHT IN CONTROLLER ❌❌❌");
        System.out.println("Exception Type: " + e.getClass().getName());
        System.out.println("Exception Message: " + e.getMessage());
        System.out.println("Stack Trace:");
        e.printStackTrace();
        System.out.println("=============================================");
        
        return ResponseEntity.status(500).body(Map.of(
            "error", e.getClass().getSimpleName(),
            "message", e.getMessage() != null ? e.getMessage() : "Unknown error",
            "details", "Check server logs for full stack trace"
        ));
    }
}

    @PutMapping("/{applicationId}/status")
    public ResponseEntity<JobApplicationResponse> updateApplicationStatus(@PathVariable Long applicationId,
                                                                        @RequestParam String status) {
        JobApplicationResponse response = applicationService.updateApplicationStatus(applicationId, status);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my-applications")
    public ResponseEntity<List<JobApplicationResponse>> getMyApplications(@AuthenticationPrincipal UserDetails principal) {
        if (principal == null) {
            logger.error("Unauthorized: No current user found for retrieving applications.");
            return ResponseEntity.status(401).build(); // 401 Unauthorized
        }

        List<JobApplicationResponse> applications = applicationService.myApplications(principal.getUsername())
                .stream()
                .map(application -> applicationService.mapToJobApplicationResponse(application))
                .collect(Collectors.toList());
        return ResponseEntity.ok(applications);
    }

    @GetMapping("/job-post/{jobPostId}")
    public ResponseEntity<List<JobApplicationResponse>> getApplicationsForJobPost(@PathVariable Long jobPostId) {
        List<JobApplicationResponse> applications = applicationService.getApplicationsForJobPost(jobPostId)
                .stream()
                .map(application -> applicationService.mapToJobApplicationResponse(application))
                .collect(Collectors.toList());
        return ResponseEntity.ok(applications);
    }

    @DeleteMapping("/{applicationId}")
    public ResponseEntity<Void> deleteApplication(@PathVariable Long applicationId) {
        applicationService.deleteApplication(applicationId);
        return ResponseEntity.noContent().build();
    }
}