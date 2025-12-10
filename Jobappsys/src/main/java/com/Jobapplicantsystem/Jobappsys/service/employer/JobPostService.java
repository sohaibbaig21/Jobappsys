package com.Jobapplicantsystem.Jobappsys.service.employer;

import com.Jobapplicantsystem.Jobappsys.dto.request.JobPostRequest;
import com.Jobapplicantsystem.Jobappsys.dto.response.JobPostResponse;
import com.Jobapplicantsystem.Jobappsys.model.JobPost;
import com.Jobapplicantsystem.Jobappsys.repository.JobPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobPostService {

    private final JobPostRepository jobPostRepository;

    // --- 1. SEARCH & FILTER (New Feature) ---
    public List<JobPostResponse> searchAndFilter(String location, String type, String sort) {
        // A. Fetch filtered results from DB
        List<JobPost> jobs = jobPostRepository.searchJobs(
                (location == null || location.isEmpty()) ? null : location,
                (type == null || type.equals("All")) ? null : type,
                null // Salary filter optional
        );

        // B. Sort in Memory
        if ("oldest".equals(sort)) {
            jobs.sort((a, b) -> a.getId().compareTo(b.getId()));
        } else if ("newest".equals(sort)) {
            jobs.sort((a, b) -> b.getId().compareTo(a.getId()));
        }
        // "relevant" keeps default DB order

        // C. Convert to DTOs
        return jobs.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    // --- 2. EXISTING CRUD OPERATIONS ---

    public JobPostResponse createJobPost(JobPostRequest request) {
        JobPost jobPost = mapToEntity(request);
        JobPost savedJob = jobPostRepository.save(jobPost);
        return mapToResponse(savedJob);
    }

    public JobPostResponse updateJobPost(Long id, JobPostRequest request) {
        JobPost jobPost = jobPostRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        // Update fields
        jobPost.setTitle(request.getTitle());
        jobPost.setDescription(request.getDescription());
        jobPost.setLocation(request.getLocation());
        jobPost.setEmploymentType(request.getEmploymentType());
        jobPost.setSalary(request.getSalary());

        JobPost updatedJob = jobPostRepository.save(jobPost);
        return mapToResponse(updatedJob);
    }

    public void deleteJobPost(Long id) {
        jobPostRepository.deleteById(id);
    }

    public JobPostResponse getJobPostById(Long id) {
        JobPost jobPost = jobPostRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        return mapToResponse(jobPost);
    }

    public List<JobPostResponse> getAllJobPosts() {
        return jobPostRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // --- 3. HELPER MAPPERS (DTO <-> Entity) ---

    private JobPostResponse mapToResponse(JobPost job) {
        JobPostResponse response = new JobPostResponse();
        response.setId(job.getId());
        response.setTitle(job.getTitle());
        response.setDescription(job.getDescription());
        response.setLocation(job.getLocation());
        response.setEmploymentType(job.getEmploymentType());
        response.setSalary(job.getSalary());
        // response.setEmployerId(job.getEmployer().getId()); // Uncomment if you linked Employer
        return response;
    }

    private JobPost mapToEntity(JobPostRequest request) {
        return JobPost.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .location(request.getLocation())
                .employmentType(request.getEmploymentType())
                .salary(request.getSalary())
                .build();
    }
}