package com.Jobapplicantsystem.Jobappsys.service.applicant;

import com.Jobapplicantsystem.Jobappsys.dto.request.ApplicationRequest;
import com.Jobapplicantsystem.Jobappsys.model.Applicant;
import com.Jobapplicantsystem.Jobappsys.model.JobApplication;
import com.Jobapplicantsystem.Jobappsys.model.JobPost;
import com.Jobapplicantsystem.Jobappsys.repository.ApplicantRepository;
import com.Jobapplicantsystem.Jobappsys.repository.JobApplicationRepository;
import com.Jobapplicantsystem.Jobappsys.repository.JobPostRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicantRepository applicantRepository;
    private final JobApplicationRepository jobApplicationRepository;
    private final JobPostRepository jobPostRepository;

    // Apply for a job
    public JobApplication apply(String email, ApplicationRequest request) {

        Applicant applicant = applicantRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Applicant profile missing"));

        JobPost jobPost = jobPostRepository.findById(request.getJobPostId())
                .orElseThrow(() -> new RuntimeException("Job not found"));

        // Prevent duplicate applications
        boolean alreadyApplied = jobApplicationRepository.findAll()
                .stream()
                .anyMatch(a -> a.getApplicant() != null
                        && a.getApplicant().getId().equals(applicant.getId())
                        && a.getJobPost() != null
                        && a.getJobPost().getId().equals(jobPost.getId()));

        if (alreadyApplied) {
            throw new RuntimeException("You have already applied to this job");
        }

        JobApplication application = JobApplication.builder()
                .applicant(applicant)
                .jobPost(jobPost)
                .build();

        return jobApplicationRepository.save(application);
    }

    // View applicant’s job applications
    public List<JobApplication> myApplications(String email) {

        Applicant applicant = applicantRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Applicant not found"));

        return jobApplicationRepository.findAll().stream()
                .filter(a -> a.getApplicant() != null && a.getApplicant().getId().equals(applicant.getId()))
                .toList();
    }
}
