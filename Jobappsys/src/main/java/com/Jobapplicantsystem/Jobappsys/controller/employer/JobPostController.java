package com.Jobapplicantsystem.Jobappsys.controller.employer;

import com.Jobapplicantsystem.Jobappsys.dto.request.JobPostRequest;
import com.Jobapplicantsystem.Jobappsys.dto.response.JobPostResponse;
import com.Jobapplicantsystem.Jobappsys.service.employer.JobPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employer/jobs")
public class JobPostController {

    @Autowired
    private JobPostService jobPostService;

    @PostMapping
    public ResponseEntity<JobPostResponse> createJob(@RequestBody JobPostRequest request) {
        return ResponseEntity.ok(jobPostService.createJobPost(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobPostResponse> updateJob(@PathVariable Long id, @RequestBody JobPostRequest request) {
        return ResponseEntity.ok(jobPostService.updateJobPost(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJob(@PathVariable Long id) {
        jobPostService.deleteJobPost(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobPostResponse> getJob(@PathVariable Long id) {
        return ResponseEntity.ok(jobPostService.getJobPostById(id));
    }

    @GetMapping
    public ResponseEntity<List<JobPostResponse>> getAllJobs() {
        return ResponseEntity.ok(jobPostService.getAllJobPosts());
    }
}