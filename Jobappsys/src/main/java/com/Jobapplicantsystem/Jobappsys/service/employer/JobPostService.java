package com.Jobapplicantsystem.Jobappsys.service.employer;

import com.Jobapplicantsystem.Jobappsys.dto.request.JobPostRequest;
import com.Jobapplicantsystem.Jobappsys.dto.response.JobPostResponse;

import java.util.List;

public interface JobPostService {
    JobPostResponse createJobPost(JobPostRequest request);
    JobPostResponse updateJobPost(Long id, JobPostRequest request);
    void deleteJobPost(Long id);
    JobPostResponse getJobPostById(Long id);
    List<JobPostResponse> getAllJobPosts();
}