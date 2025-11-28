package com.Jobapplicantsystem.Jobappsys.service.applicant;

import com.Jobapplicantsystem.dto.request.ApplicationRequest;
import com.Jobapplicantsystem.model.*;
import com.Jobapplicantsystem.repository.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final UserRepository userRepository;
    private final ApplicantRepository applicantRepository;
    private final JobApplicationRepository jobApplicationRepository;
    private final JobPostRepository jobPostRepository;

    // Apply for a job
    public JobApplication apply(String email, ApplicationRequest request) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Applicant applicant = applicantRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("Applicant profile missing"));

        JobPost jobPost = jobPostRepository.findById(request.getJobId())
                .orElseThrow(() -> new RuntimeException("Job not found"));

        // Prevent duplicate applications
        boolean alreadyApplied = jobApplicationRepository.findAll()
                .stream()
                .anyMatch(a -> a.getApplicant().getId().equals(user.getId())
                        && a.getJobPost().getId().equals(request.getJobId()));

        if (alreadyApplied) {
            throw new RuntimeException("You have already applied to this job");
        }

        JobApplication application = JobApplication.builder()
                .applicant(applicant)
                .jobPost(jobPost)
                .coverLetter(request.getCoverLetter())
                .status("PENDING")
                .appliedAt(LocalDateTime.now())
                .build();

        return jobApplicationRepository.save(application);
    }

    // View applicant’s job applications
    public List<JobApplication> myApplications(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return jobApplicationRepository.findAll().stream()
                .filter(a -> a.getApplicant().getId().equals(user.getId()))
                .toList();
    }
}
