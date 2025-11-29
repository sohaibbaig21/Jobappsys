package com.Jobapplicantsystem.Jobappsys.service.applicant;

import com.Jobapplicantsystem.Jobappsys.model.Applicant;
import com.Jobapplicantsystem.Jobappsys.model.JobPost;
import com.Jobapplicantsystem.Jobappsys.model.SavedJob;
import com.Jobapplicantsystem.Jobappsys.repository.ApplicantRepository;
import com.Jobapplicantsystem.Jobappsys.repository.JobPostRepository;
import com.Jobapplicantsystem.Jobappsys.repository.SavedJobRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicantService {

    private final ApplicantRepository applicantRepository;
    private final JobPostRepository jobPostRepository;
    private final SavedJobRepository savedJobRepository;

    // Save job bookmark
    public SavedJob saveJob(String email, Long jobId) {

        Applicant applicant = applicantRepository.findAll().stream()
                .filter(a -> email.equalsIgnoreCase(a.getEmail()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Applicant not found"));

        JobPost job = jobPostRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        SavedJob savedJob = SavedJob.builder()
                .applicant(applicant)
                .jobPost(job)
                .build();

        return savedJobRepository.save(savedJob);
    }

    // Get saved jobs
    public List<SavedJob> getSavedJobs(String email) {

        Applicant applicant = applicantRepository.findAll().stream()
                .filter(a -> email.equalsIgnoreCase(a.getEmail()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Applicant not found"));

        return savedJobRepository.findAll().stream()
                .filter(saved -> saved.getApplicant() != null && saved.getApplicant().getId().equals(applicant.getId()))
                .toList();
    }

    // Get all job posts (for searching)
    public List<JobPost> getAllJobs() {
        return jobPostRepository.findAll();
    }
}
