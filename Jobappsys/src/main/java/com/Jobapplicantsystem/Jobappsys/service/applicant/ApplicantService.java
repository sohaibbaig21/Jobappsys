package com.Jobapplicantsystem.Jobappsys.service.applicant;

import com.Jobapplicantsystem.model.JobPost;
import com.Jobapplicantsystem.model.SavedJob;
import com.Jobapplicantsystem.model.User;
import com.Jobapplicantsystem.repository.JobPostRepository;
import com.Jobapplicantsystem.repository.SavedJobRepository;
import com.Jobapplicantsystem.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicantService {

    private final UserRepository userRepository;
    private final JobPostRepository jobPostRepository;
    private final SavedJobRepository savedJobRepository;

    // Save job bookmark
    public SavedJob saveJob(String email, Long jobId) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        JobPost job = jobPostRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        SavedJob savedJob = SavedJob.builder()
                .user(user)
                .jobPost(job)
                .build();

        return savedJobRepository.save(savedJob);
    }

    // Get saved jobs
    public List<SavedJob> getSavedJobs(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return savedJobRepository.findAll().stream()
                .filter(job -> job.getUser().getId().equals(user.getId()))
                .toList();
    }

    // Get all job posts (for searching)
    public List<JobPost> getAllJobs() {
        return jobPostRepository.findAll();
    }
}
