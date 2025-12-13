package com.Jobapplicantsystem.Jobappsys.controller.employer;

import com.Jobapplicantsystem.Jobappsys.dto.request.JobPostRequest;
import com.Jobapplicantsystem.Jobappsys.dto.response.JobPostResponse;
import com.Jobapplicantsystem.Jobappsys.repository.JobApplicationRepository;
import com.Jobapplicantsystem.Jobappsys.repository.JobPostRepository;
import com.Jobapplicantsystem.Jobappsys.service.employer.JobPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map; // <--- Needed for the Map.of

@RestController
@RequestMapping("/api/employer/jobs")
@RequiredArgsConstructor
public class EmployerJobController {

    private final JobPostService jobPostService;

    // NEW: Inject these repositories to fetch applicants
    private final JobPostRepository jobPostRepository;
    private final JobApplicationRepository jobApplicationRepository;

    // POST: Create a job
    @PostMapping
    public ResponseEntity<JobPostResponse> createJob(@RequestBody JobPostRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(jobPostService.createJobPost(request, email));
    }

    // GET: Get ONLY my posted jobs
    @GetMapping("/my-jobs")
    public ResponseEntity<List<JobPostResponse>> getMyJobs() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(jobPostService.getMyJobs(email));
    }

    // NEW ENDPOINT: Get applicants for a specific job (With Null Safety)
    @GetMapping("/{jobId}/applicants")
    public ResponseEntity<?> getJobApplicants(@PathVariable Long jobId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        // 1. Verify the job belongs to this employer
        if (jobId == null) {
            throw new RuntimeException("Job ID cannot be null");
        }
        var job = jobPostRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (!job.getEmployer().getEmail().equals(email)) {
            return ResponseEntity.status(403).body("Unauthorized: You do not own this job post.");
        }

        // 2. Fetch applications
        var applications = jobApplicationRepository.findByJobPostId(jobId);

        // 3. Map to simple DTO (Handling NULLs to prevent crashes)
        var response = applications.stream().map(app -> {
            var applicant = app.getApplicant();
            // Guard against unexpected nulls
            if (applicant == null) {
                return Map.of(
                        "id", app.getId() != null ? app.getId().toString() : "",
                        "applicantName", "Unknown Applicant",
                        "applicantEmail", "",
                        "applicantPhone", "N/A",
                        "status", app.getStatus() != null ? app.getStatus() : "UNKNOWN",
                        "appliedDate", app.getAppliedAt() != null ? app.getAppliedAt().toString() : "",
                        "answers", app.getAnswers() != null ? app.getAnswers().stream().map(ans -> Map.of("question", ans.getJobQuestion() != null ? ans.getJobQuestion().getQuestionText() : "", "answer", ans.getAnswerText() != null ? ans.getAnswerText() : "")).toList().toString() : "[]",
                        "skills", "[]",
                        "resumeFilename", app.getResumeFilename() != null ? app.getResumeFilename() : ""
                );
            }

            var user = applicant.getUser();

            // Safety checks and sane defaults
            String phone = applicant.getPhone() != null ? applicant.getPhone() : "N/A";
            String answers = app.getAnswers() != null ? app.getAnswers().stream().map(ans -> Map.of("question", ans.getJobQuestion() != null ? ans.getJobQuestion().getQuestionText() : "", "answer", ans.getAnswerText() != null ? ans.getAnswerText() : "")).toList().toString() : "[]";
            String skills = applicant.getSkills() != null ? applicant.getSkills().stream().map(skill -> Map.of("name", skill.getSkillName() != null ? skill.getSkillName() : "", "proficiency", skill.getProficiencyLevel() != null ? skill.getProficiencyLevel() : "")).toList().toString() : "[]";
            String statusStr = app.getStatus() != null ? app.getStatus() : "APPLIED";
            String appliedDate = app.getAppliedAt() != null ? app.getAppliedAt().toString() : "";

            String firstName = (user != null && user.getFirstName() != null) ? user.getFirstName() : "";
            String lastName = (user != null && user.getLastName() != null) ? user.getLastName() : "";
            String fullName = (firstName + " " + lastName).trim();
            if (fullName.isEmpty()) fullName = "Unknown Applicant";

            String emailStr = (user != null && user.getEmail() != null) ? user.getEmail() : "";

            return Map.of(
                    "id", app.getId().toString(),
                    "applicantName", fullName,
                    "applicantEmail", emailStr,
                    "applicantPhone", phone,
                    "status", statusStr,
                    "appliedDate", appliedDate,
                    "answers", answers,
                    "skills", skills,
                    "resumeFilename", app.getResumeFilename() != null ? app.getResumeFilename() : ""
            );
        }).toList();

        return ResponseEntity.ok(response);
    }
}