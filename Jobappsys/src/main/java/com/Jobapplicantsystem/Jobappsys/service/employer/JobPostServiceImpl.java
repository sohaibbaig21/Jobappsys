package com.Jobapplicantsystem.Jobappsys.service.employer;

import com.Jobapplicantsystem.Jobappsys.dto.request.JobPostRequest;
import com.Jobapplicantsystem.Jobappsys.dto.response.JobPostResponse;
import com.Jobapplicantsystem.Jobappsys.model.JobPost;
import com.Jobapplicantsystem.Jobappsys.repository.JobPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobPostServiceImpl implements JobPostService {

    @Autowired
    private JobPostRepository jobPostRepository;

    @Override
    public JobPostResponse createJobPost(JobPostRequest request) {
        // 1. Map Request DTO to Entity
        JobPost jobPost = new JobPost();
        jobPost.setTitle(request.getTitle());
        jobPost.setDescription(request.getDescription());
        jobPost.setLocation(request.getLocation());
        jobPost.setEmploymentType(request.getEmploymentType());
        jobPost.setSalary(request.getSalary());

        // NOTE: Later you will need to set the 'Employer' here based on the logged-in user.
        // For now, we save it without an employer to get the app running.

        // 2. Save to Database
        JobPost savedJob = jobPostRepository.save(jobPost);

        // 3. Return Response
        return mapToResponse(savedJob);
    }

    @Override
    public JobPostResponse updateJobPost(Long id, JobPostRequest request) {
        JobPost jobPost = jobPostRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job Post not found with id: " + id));

        // Update fields
        jobPost.setTitle(request.getTitle());
        jobPost.setDescription(request.getDescription());
        jobPost.setLocation(request.getLocation());
        jobPost.setEmploymentType(request.getEmploymentType());
        jobPost.setSalary(request.getSalary());

        JobPost updatedJob = jobPostRepository.save(jobPost);
        return mapToResponse(updatedJob);
    }

    @Override
    public void deleteJobPost(Long id) {
        if (!jobPostRepository.existsById(id)) {
            throw new RuntimeException("Job Post not found with id: " + id);
        }
        jobPostRepository.deleteById(id);
    }

    @Override
    public JobPostResponse getJobPostById(Long id) {
        JobPost jobPost = jobPostRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job Post not found with id: " + id));
        return mapToResponse(jobPost);
    }

    @Override
    public List<JobPostResponse> getAllJobPosts() {
        return jobPostRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // --- Helper Method to Map Entity to Response DTO ---
    private JobPostResponse mapToResponse(JobPost jobPost) {
        JobPostResponse response = new JobPostResponse();

        response.setId(jobPost.getId());
        response.setTitle(jobPost.getTitle());
        response.setDescription(jobPost.getDescription());
        response.setLocation(jobPost.getLocation());
        response.setEmploymentType(jobPost.getEmploymentType());
        response.setSalary(jobPost.getSalary());

        // Safety check: Only get Employer ID if an employer is actually assigned
        if (jobPost.getEmployer() != null) {
            response.setEmployerId(jobPost.getEmployer().getId());
        }

        return response;
    }
}
